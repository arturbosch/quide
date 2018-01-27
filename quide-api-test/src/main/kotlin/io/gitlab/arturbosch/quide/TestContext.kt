package io.gitlab.arturbosch.quide

import io.gitlab.arturbosch.quide.api.AnalysisContext
import io.gitlab.arturbosch.quide.api.Plugin
import io.gitlab.arturbosch.quide.api.core.PropertiesAware
import io.gitlab.arturbosch.quide.api.core.QuideDir
import io.gitlab.arturbosch.quide.api.core.StorageAware
import io.gitlab.arturbosch.quide.api.core.VersionAware
import io.gitlab.arturbosch.quide.api.filesystem.FileSystem
import io.gitlab.arturbosch.quide.api.core.ResourceAware
import java.io.File
import java.io.IOException
import java.util.HashMap

/**
 * @author Artur Bosch
 */
abstract class AbstractAnalysisContext(override val pluginId: String,
									   private val storage: Storage,
									   override val fileSystem: FileSystem,
									   override val quideDirectory: QuideDirectory) : AnalysisContext,
		StorageAware by storage,
		VersionAware, ResourceAware,
		PropertiesAware by quideDirectory,
		FileSystem by fileSystem

class TestContext(plugin: Plugin) : AbstractAnalysisContext(
		plugin.id, TestStorage, TestFileSystem, TestQuideDir)

object TestQuideDir : QuideDirectory()
object TestStorage : Storage()

abstract class Storage(private val storage: HashMap<String, Any> = HashMap()) : StorageAware {

	@Suppress("UNCHECKED_CAST")
	override operator fun <T> get(key: String): T? = storage[key] as? T?

	override fun <T : Any> put(key: String, value: T) {
		storage[key] = value
	}

}

abstract class QuideDirectory(private val properties: MutableMap<String, String> = HashMap()) : QuideDir {

	companion object {
		const val USER_HOME = "user.home"
		const val QUIDE_DIR_NAME = ".quide"
		const val PLUGINS_DIR_NAME = "plugins"
		const val CONFIGURATIONS_DIR_NAME = "configurations"
	}

	override val home: File = checkDir(File(System.getProperty(USER_HOME), QUIDE_DIR_NAME))
	override val pluginsDir: File = checkDir(resolve(PLUGINS_DIR_NAME))
	override val configurationsDir: File = checkDir(resolve(CONFIGURATIONS_DIR_NAME))

	final override fun resolve(subPath: String): File {
		return checkDir(home.resolve(subPath))
	}

	override fun property(key: String): String? = properties[key]
	override fun propertyOrDefault(key: String, defaultValue: String): String = properties[key] ?: defaultValue

	private fun checkDir(path: File): File {
		if (!path.exists() && !path.mkdirs()) {
			throw IOException("Error creating directories for " + path)
		}
		return path
	}

}
