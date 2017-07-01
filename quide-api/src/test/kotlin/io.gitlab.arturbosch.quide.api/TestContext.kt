package io.gitlab.arturbosch.quide.api

import io.gitlab.arturbosch.quide.api.core.PropertiesAware
import io.gitlab.arturbosch.quide.api.core.QuideDirectory
import io.gitlab.arturbosch.quide.api.core.StorageAware
import io.gitlab.arturbosch.quide.api.core.VersionAware
import io.gitlab.arturbosch.quide.api.filesystem.FileSystem
import io.gitlab.arturbosch.quide.api.utils.ResourceAware
import java.util.HashMap

/**
 * @author Artur Bosch
 */
abstract class AbstractAnalysisContext(override val pluginId: String,
									   private val storage: Storage,
									   override val fileSystem: FileSystem,
									   override val quideDirectory: QuideDirectory)
	: AnalysisContext,
		StorageAware by storage,
		VersionAware, ResourceAware,
		PropertiesAware by quideDirectory,
		FileSystem by fileSystem

class TestContext(plugin: Plugin, context: Plugin.Context) : AbstractAnalysisContext(
		plugin.id, TestStorage, TestFileSystem, TestQuideDir)

object TestQuideDir : QuideDirectory()
object TestStorage : Storage()

abstract class Storage(protected val storage: HashMap<String, Any> = HashMap()) : StorageAware {

	@Suppress("UNCHECKED_CAST")
	override operator fun <T> get(key: String): T? = storage[key] as T?

	override fun <T : Any> put(key: String, value: T) {
		storage.put(key, value)
	}

}