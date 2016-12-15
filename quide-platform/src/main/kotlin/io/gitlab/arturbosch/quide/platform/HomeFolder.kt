package io.gitlab.arturbosch.quide.platform

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Artur Bosch
 */
object HomeFolder {

	private val lazyDir = lazy {
		Paths.get(System.getProperty("user.home"), ".quide", "plugins").apply {
			if (Files.notExists(this)) Files.createDirectories(this)
		}
	}

	val pluginDirectory: Path = lazyDir.value
}