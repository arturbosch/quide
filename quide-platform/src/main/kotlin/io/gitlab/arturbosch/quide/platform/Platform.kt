package io.gitlab.arturbosch.quide.platform

import io.gitlab.arturbosch.kutils.awaitAll
import io.gitlab.arturbosch.kutils.cores
import io.gitlab.arturbosch.kutils.runAsync
import io.gitlab.arturbosch.kutils.withExecutor
import io.gitlab.arturbosch.kutils.withNamedThreadPoolExecutor
import io.gitlab.arturbosch.quide.model.CodeSmell
import io.gitlab.arturbosch.quide.model.SmellContainer
import io.gitlab.arturbosch.quide.vcs.VersionProvider
import io.gitlab.arturbosch.quide.vcs.Versionable
import java.nio.file.Files
import java.util.concurrent.ExecutorService

/**
 * @author Artur Bosch
 */
class QuidePlatform(analysis: Analysis,
					vcsLoader: VCSLoader,
					pluginLoader: PluginLoader) {

	private val logger by logFactory()

	private val executablePlatform: BasePlatform

	init {
		val provider = vcsLoader.load()
		val multiPlatform = provider?.let { MultiPlatform(provider) }
		executablePlatform = BasePlatform(analysis, pluginLoader, multiPlatform)
	}

	fun analyze() {
		logger.info("Starting $QUIDE ...")
		executablePlatform.analyze()
	}
}

class MultiPlatform(private val versionProvider: VersionProvider) {

	private val logger by logFactory()

	fun registerVersionProvider(plugins: List<Plugin>) {
		plugins.forEach { it.userData().put(VersionAware.VERSION_PROVIDER, versionProvider) }
	}

	fun analyze(plugins: List<Plugin>, runBlock: () -> Unit) {
		var lastVersion: Versionable? = null
		var currentVersion = versionProvider.nextVersion()
		while (currentVersion.isPresent) {
			val current = currentVersion.get()
			logger.info(current.asPrintable())
			plugins.forEach { it.userData().put(UserData.LAST_VERSION, lastVersion) }
			plugins.forEach { it.userData().put(UserData.CURRENT_VERSION, current) }
			runBlock()
			lastVersion = current
			currentVersion = versionProvider.nextVersion()
		}
	}
}

class BasePlatform(private val analysis: Analysis,
				   pluginLoader: PluginLoader,
				   private val multiPlatform: MultiPlatform? = null) : ControlFlow {

	private val logger by logFactory()

	private val _plugins = pluginLoader.load()

	private val cpuCores: Int = Math.min(plugins().size, cores)

	private val isDebugMode = QuideConstants.DEBUG.asProperty()?.toBoolean() ?: false

	override fun plugins(): List<Plugin> = _plugins

	fun analyze() {
		if (cpuCores < 1) {
			logger.info("No plugins available...shutting down!")
			return
		}
		run(analysis)
	}

	override fun run(context: AnalysisContext) {
		if (isDebugMode) logger.info("Debug is active.")
		multiPlatform?.registerVersionProvider(plugins())
		beforeAnalysis(context)
		withExecutor(withNamedThreadPoolExecutor(QUIDE + "-", cpuCores)) {
			multiPlatform?.analyze(plugins()) { runPlugins() } ?: runPlugins()
		}
		if (isDebugMode) debugOutput()
		afterAnalysis()
	}

	private fun ExecutorService.runPlugins() {
		val futures = plugins().map { plugin ->
			runAsync {
				execute(plugin)
			}.exceptionally {
				logger.error("An error occurred while executing ${plugin.name()}", it)
			}
		}
		awaitAll(futures)
	}

	private fun debugOutput() {

		fun List<CodeSmell>.smellsToBytes() = map { it.toString() }.sorted().joinToString("\n").toByteArray()

		plugins().forEach {
			it.userData().currentContainer<SmellContainer<CodeSmell>, CodeSmell>().ifPresent {
				println()
				logger.info("#All: " + it.all().size)
				logger.info("#Alive: " + it.alive().size)
				logger.info("#Dead: " + it.dead().size)
				Files.write(HomeFolder.resolve("alltestrun.txt"), it.all().smellsToBytes())
				Files.write(HomeFolder.resolve("alivetestrun.txt"), it.alive().smellsToBytes())
				Files.write(HomeFolder.resolve("deadtestrun.txt"), it.dead().smellsToBytes())
				it.all().filter { it.startVersion() == null }.forEach {
					logger.info("UPS - $it")
				}
			}
		}
	}

}