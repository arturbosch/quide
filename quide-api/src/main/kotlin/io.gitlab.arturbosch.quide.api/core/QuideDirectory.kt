package io.gitlab.arturbosch.quide.api.core

import java.io.File
import java.io.IOException
import java.util.HashMap

/**
 * @author Artur Bosch
 */
abstract class QuideDirectory(protected val properties: MutableMap<String, String> = HashMap()) : PropertiesAware {

	companion object {
		const val USER_HOME = "user.home"
		const val QUIDE_DIR_NAME = ".quide"
		const val PLUGINS_DIR_NAME = "plugins"
		const val CONFIGURATIONS_DIR_NAME = "configurations"
	}

	val home: File = checkDir(File(System.getProperty(USER_HOME), QUIDE_DIR_NAME))
	val pluginsDir: File = checkDir(resolve(PLUGINS_DIR_NAME))
	val configurationsDir: File = checkDir(resolve(CONFIGURATIONS_DIR_NAME))

	fun resolve(subPath: String): File {
		return checkDir(home.resolve(subPath))
	}

	override fun property(key: String): String? {
		return properties[key]
	}

	override fun propertyOrDefault(key: String, defaultValue: String): String {
		return properties[key] ?: defaultValue
	}

	private fun checkDir(path: File): File {
		if (!path.exists()) {
			if (!path.mkdirs()) {
				throw IOException("Error creating directories for " + path)
			}
		}
		return path
	}

}
