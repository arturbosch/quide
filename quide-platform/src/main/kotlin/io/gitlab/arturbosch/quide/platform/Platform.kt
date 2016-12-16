package io.gitlab.arturbosch.quide.platform

import io.gitlab.arturbosch.kutils.awaitAll
import io.gitlab.arturbosch.kutils.runAsync
import io.gitlab.arturbosch.kutils.withExecutor
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
class Platform(private val pluginLoader: PluginLoader) : ControlFlow {

	private val logger by logFactory()

	private val _plugins = lazy {
		pluginLoader.load()
	}

	override fun plugins(): List<Plugin> {
		return _plugins.value
	}

	fun analyze(path: Path) {
		logger.info("Starting quide ...")
		withExecutor {
			val futures = plugins().map {
				runAsync {
					execute(it, path)
				}
			}
			awaitAll(futures)
		}
	}

}