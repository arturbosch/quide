package io.gitlab.arturbosch.quide.platform.analysis

import io.gitlab.arturbosch.kutils.runAsync
import io.gitlab.arturbosch.kutils.withExecutor
import io.gitlab.arturbosch.quide.api.AnalysisContext
import io.gitlab.arturbosch.quide.api.Plugin
import io.gitlab.arturbosch.quide.core.Storage
import io.gitlab.arturbosch.quide.core.context.DefaultAnalysisContext
import io.gitlab.arturbosch.quide.core.fs.DefaultFileSystem
import io.gitlab.arturbosch.quide.platform.HomeFolder
import io.gitlab.arturbosch.quide.platform.cli.Args
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
			Files.createTempDirectory("quide").toFile())

	private fun prepare(): Sequence<Pair<Plugin.Context, AnalysisContext>> = plugins.asSequence()
			.map { definePlugin(it) to defineAnalysisContext(it) }

	private fun defineAnalysisContext(it: Plugin) = DefaultAnalysisContext(
			it.id,
			Storage(),
			fs,
			HomeFolder)

	private fun definePlugin(it: Plugin) = Plugin.Context().apply { it.define(this) }

	override fun run() {
		withExecutor(service) {
			prepare().forEach { (pluginContext, analysisContext) ->
				runAsync {
					pluginContext.detectorInstance?.execute(analysisContext)
				}
			}
		}
	}
}
