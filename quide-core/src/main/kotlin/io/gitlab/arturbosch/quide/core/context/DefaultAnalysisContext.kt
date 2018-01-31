package io.gitlab.arturbosch.quide.core.context

import io.gitlab.arturbosch.quide.api.AnalysisContext
import io.gitlab.arturbosch.quide.api.core.QuideDir
import io.gitlab.arturbosch.quide.api.core.ResourceAware
import io.gitlab.arturbosch.quide.api.core.StorageAware
import io.gitlab.arturbosch.quide.api.core.VersionAware
import io.gitlab.arturbosch.quide.api.fs.FileSystem

/**
 * @author Artur Bosch
 */
open class DefaultAnalysisContext(override val pluginId: String,
								  private val storage: StorageAware,
								  override val fileSystem: FileSystem,
								  override val quideDirectory: QuideDir)
	: AnalysisContext,
		StorageAware by storage,
		VersionAware, ResourceAware,
		QuideDir by quideDirectory,
		FileSystem by fileSystem
