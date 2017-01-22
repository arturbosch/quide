package io.gitlab.arturbosch.quide.platform

import io.gitlab.arturbosch.kutils.awaitAll
import io.gitlab.arturbosch.kutils.runAsync
import io.gitlab.arturbosch.kutils.withExecutor
import io.gitlab.arturbosch.kutils.withNamedThreadPoolExecutor
import io.gitlab.arturbosch.quide.vcs.VersionProvider
import io.gitlab.arturbosch.quide.vcs.Versionable

/**
 * @author Artur Bosch
 */
interface Platform {
	fun analyze()
}

class QuidePlatform(vcsLoader: VCSLoader,
					platform: BasePlatform) : Platform {

	private val logger by logFactory()
	private val executablePlatform: Platform

	init {
		val provider = vcsLoader.load()

		if (provider != null) {
			executablePlatform = MultiPlatform(platform, provider)
		} else {
			executablePlatform = platform
		}
	}

	override fun analyze() {
		logger.info("Starting $QUIDE ...")
		executablePlatform.analyze()
	}
}

class MultiPlatform(private val platform: BasePlatform,
					private val versionProvider: VersionProvider) : Platform {

	private val logger by logFactory()

	override fun analyze() {
		var lastVersion: Versionable? = null
		var currentVersion = versionProvider.nextVersion()
		while (currentVersion.isPresent) {
			val current = currentVersion.get()
			logger.info("${current.versionNumber()} - ${current.revision().date()} - ${current.revision().message()}")
			platform.plugins().forEach { it.userData().put(UserData.LAST_VERSION, lastVersion) }
			platform.plugins().forEach { it.userData().put(UserData.CURRENT_VERSION, current) }
			platform.analyze()
			lastVersion = current
			currentVersion = versionProvider.nextVersion()
		}
	}
}

class BasePlatform(private val analysis: Analysis,
				   private val pluginLoader: PluginLoader) : ControlFlow, Platform {

	private val logger by logFactory()

	private val _plugins = lazy {
		pluginLoader.load()
	}

	override fun plugins(): List<Plugin> {
		return _plugins.value
	}

	override fun analyze() {
		val cores = plugins().size
		withExecutor(withNamedThreadPoolExecutor(QUIDE, cores)) {
			val futures = plugins().map { plugin ->
				runAsync {
					execute(plugin, analysis)
				}.exceptionally {
					logger.error("An error occurred while executing ${plugin.name()}", it)
				}
			}
			awaitAll(futures)
		}
	}

}