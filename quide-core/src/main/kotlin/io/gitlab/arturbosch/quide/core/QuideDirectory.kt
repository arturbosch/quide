package io.gitlab.arturbosch.quide.core

import io.gitlab.arturbosch.quide.api.core.PLATFORM_ADDITIONAL_PROPERTIES
import io.gitlab.arturbosch.quide.api.core.QuideDir
import mu.KLogging
import java.io.File
import java.io.IOException
import java.util.HashMap
import java.util.Properties

/**
 * @author Artur Bosch
 */
open class QuideDirectory(
		private val homeDir: File,
		protected val properties: MutableMap<String, String> = HashMap()) : QuideDir {

	companion object : KLogging() {
		const val PLUGINS_DIR_NAME = "plugins"
		const val CONFIGURATIONS_DIR_NAME = "configurations"
	}

	init {
		val propertiesPath = homeDir.resolve("quide.properties")
		if (!propertiesPath.exists()) {
			javaClass.getResourceAsStream("/quide.properties").use { `in` ->
				`in`.copyTo(propertiesPath.outputStream())
				logger.info("Created default properties set.")
			}
		} else {
			loadProperties(propertiesPath)
			loadAdditionalProperties()
			logger.info("Loaded quide.properties.")
		}
	}

	override val home: File get() = checkDir(homeDir)
	override val pluginsDir: File get() = checkDir(resolve(PLUGINS_DIR_NAME))
	override val configurationsDir: File get() = checkDir(resolve(CONFIGURATIONS_DIR_NAME))

	final override fun resolve(subPath: String): File {
		return home.resolve(subPath)
	}

	override fun property(key: String): String? = properties[key]?.let { if (it.isEmpty()) null else it }
	override fun propertyOrDefault(key: String, defaultValue: String): String = property(key) ?: defaultValue

	private fun loadAdditionalProperties() {
		val additional = property(PLATFORM_ADDITIONAL_PROPERTIES)
		if (additional != null) {
			loadPropertiesFromString(additional)
		}
	}

	protected fun loadPropertiesFromString(commaSeparatedPaths: String) {
		commaSeparatedPaths.split(",".toRegex())
				.dropLastWhile { it.isEmpty() }
				.toTypedArray()
				.map { it.trim() }
				.map { File(it) }
				.forEach { this.loadProperties(it) }
	}

	private fun loadProperties(propertiesPath: File) {
		propertiesPath.inputStream().use { `is` ->
			val props = Properties()
			props.load(`is`)
			@Suppress("UNCHECKED_CAST")
			properties.putAll(props as Map<String, String>)
			logger.info("Loaded extra properties from '$propertiesPath'.")
		}
	}

	private fun checkDir(path: File): File {
		if (!path.exists() && !path.mkdirs()) {
			throw IOException("Error creating directories for " + path)
		}
		return path
	}

}
