package io.gitlab.arturbosch.quide.platform

import io.gitlab.arturbosch.kutils.toList
import java.net.URL
import java.nio.file.Files

/**
 * @author Artur Bosch
 */
interface PluginDetector {
	val jars: Array<URL>
}

object BasePluginDetector : PluginDetector {

	private val logger by logFactory()

	private val lazyPlugins = lazy {
		Files.list(HomeFolder.pluginDirectory)
				.filter { it.toString().endsWith(".jar") }
				.map { it.toUri().toURL() }
				.toList().apply {
			logger.info("Jars found: " + joinToString { it.path.substringAfterLast('/') })
		}
	}

	override val jars: Array<URL>
		get() = lazyPlugins.value.toTypedArray()
}

