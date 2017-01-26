package io.gitlab.arturbosch.quide.platform

import java.nio.file.Files
import java.nio.file.Path
import java.util.Properties

/**
 * @author Artur Bosch
 */
object HomeFolder : QuideDirectory {
	val propertiesPath: Path = home().resolve("quide.properties")
}

object QuideProperties {
	private val properties = Properties().apply {
		val propertiesPath = HomeFolder.propertiesPath
		if (Files.notExists(propertiesPath)) Files.createFile(propertiesPath)
		Files.newInputStream(propertiesPath).use {
			load(it)
		}
	}

	fun getOrNull(key: String): String?  {
		return properties.getProperty(key)
	}
}

fun String.asProperty(): String? = QuideProperties.getOrNull(this)