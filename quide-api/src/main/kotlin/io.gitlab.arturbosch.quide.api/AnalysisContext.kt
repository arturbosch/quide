package io.gitlab.arturbosch.quide.api

import io.gitlab.arturbosch.quide.api.core.PropertiesAware
import io.gitlab.arturbosch.quide.api.core.QuideDirectory
import io.gitlab.arturbosch.quide.api.core.Storage
import io.gitlab.arturbosch.quide.api.core.StorageAware
import io.gitlab.arturbosch.quide.api.core.VersionAware
import io.gitlab.arturbosch.quide.api.filesystem.FileSystem
import io.gitlab.arturbosch.quide.api.utils.ResourceAware

/**
 * @author Artur Bosch
 */
interface AnalysisContext : StorageAware, VersionAware, ResourceAware, PropertiesAware, FileSystem {
	val pluginId: String
	val fileSystem: FileSystem
	val quideDirectory: QuideDirectory
}

abstract class AbstractAnalysisContext(override val pluginId: String,
									   private val storage: Storage,
									   override val fileSystem: FileSystem,
									   override val quideDirectory: QuideDirectory)
	: AnalysisContext,
		StorageAware by storage,
		VersionAware, ResourceAware,
		PropertiesAware by quideDirectory,
		FileSystem by fileSystem