package io.gitlab.arturbosch.quide.platform.analysis

import io.gitlab.arturbosch.kutils.awaitAll
import io.gitlab.arturbosch.kutils.task
import io.gitlab.arturbosch.kutils.withExecutor
import io.gitlab.arturbosch.quide.api.AnalysisContext
import io.gitlab.arturbosch.quide.api.Plugin
import io.gitlab.arturbosch.quide.api.processors.InjectionPoint
import io.gitlab.arturbosch.quide.core.Storage
import io.gitlab.arturbosch.quide.core.context.DefaultAnalysisContext
import io.gitlab.arturbosch.quide.core.fs.DefaultFileSystem
import io.gitlab.arturbosch.quide.platform.HomeFolder
import io.gitlab.arturbosch.quide.platform.cli.Args
import mu.KLogging
import java.nio.file.Files
import java.util.concurrent.ExecutorService
import java.util.concurrent.ForkJoinPool

/**
 * @author Artur Bosch
 */
class Platform(args: Args,
			   private val plugins: List<Plugin>) : Runnable {

	private val service: ExecutorService = ForkJoinPool.commonPool()
	private val fs = DefaultFileSystem(
			args.input.toFile(),
			(args.output ?: Files.createTempDirectory("quide")).toFile())

	private fun prepare(): Map<Plugin.Context, AnalysisContext> = plugins
			.map { definePlugin(it) to defineAnalysisContext(it) }
			.toMap()

	private fun defineAnalysisContext(plugin: Plugin) = DefaultAnalysisContext(
			plugin.id,
			Storage(),
			fs,
			HomeFolder)

	private fun definePlugin(plugin: Plugin) = Plugin.Context().apply { plugin.define(this) }

	override fun run() {
		logger.info("Starting analysis.")
		withExecutor(service) {
			val containers = prepare()
			containers.runProcessorsBy(InjectionPoint.BeforeAnalysis)
			containers.runProcessorsBy(InjectionPoint.BeforeDetection)
			containers.runDetectors()
			containers.runProcessorsBy(InjectionPoint.AfterDetection)
			containers.runProcessorsBy(InjectionPoint.AfterAnalysis)
		}
		logger.info("Finished analysis.")
	}

	private fun Map<Plugin.Context, AnalysisContext>.runDetectors() {
		awaitAll(map { (pluginContext, analysisContext) ->
			task(service) {
				val detector = pluginContext.detectorInstance
				logger.info("Running ${detector?.name}.")
				detector?.execute(analysisContext)
			}
		})
	}

	private fun Map<Plugin.Context, AnalysisContext>.runProcessorsBy(ip: InjectionPoint) {
		awaitAll(map { (pc, ac) ->
			val processors = pc.registeredProcessors.filter { it.injectionPoint() == ip }
			task(service) {
				processors.forEach {
					logger.info("Running ${it.name}.")
					it.execute(ac)
				}
			}
		})
	}

	companion object : KLogging()
}
