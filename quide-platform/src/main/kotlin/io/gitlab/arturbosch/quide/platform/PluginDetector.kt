package io.gitlab.arturbosch.quide.platform

import io.gitlab.arturbosch.kutils.toList
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
interface PluginDetector {
	val jars: Array<URL>
}

object BasePluginDetector : PluginDetector {

	private val logger by logFactory()

	private val lazyPlugins = lazy {
		Files.list(HomeFolder.pluginsDir())
				.filter { it.toString().endsWith(".jar") }
				.filter { !ignoredPlugin(it) }
				.map { it.toUri().toURL() }
				.toList().apply {
			logger.info("Jars found: " + joinToString { it.path.substringAfterLast('/') })
		}
	}

	private fun ignoredPlugin(path: Path): Boolean {
		val filename = path.fileName.toString().substringBeforeLast(".")
		val property = QuideConstants.PLATFORM_IGNORE_PLUGINS.asProperty() ?: ""
		val toIgnore = property.split(",").map(String::trim)
		return filename in toIgnore
	}

	override val jars: Array<URL>
		get() = lazyPlugins.value.toTypedArray()
}

