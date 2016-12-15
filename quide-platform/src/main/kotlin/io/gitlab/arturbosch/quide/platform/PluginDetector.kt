package io.gitlab.arturbosch.quide.platform

import io.gitlab.arturbosch.kutils.toList
import java.net.URL
import java.nio.file.Files

/**
 * @author Artur Bosch
 */
class PluginDetector {

	fun search(): List<URL> {
		return Files.list(HomeFolder.pluginDirectory)
				.filter { it.toString().endsWith(".jar") }
				.map { it.toUri().toURL() }
				.toList()
	}
}

