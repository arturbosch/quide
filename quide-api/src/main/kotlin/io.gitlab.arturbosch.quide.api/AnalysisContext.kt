package io.gitlab.arturbosch.quide.api

import io.gitlab.arturbosch.quide.api.core.PropertiesAware
import io.gitlab.arturbosch.quide.api.core.QuideDir
import io.gitlab.arturbosch.quide.api.core.StorageAware
import io.gitlab.arturbosch.quide.api.core.VersionAware
import io.gitlab.arturbosch.quide.api.fs.FileSystem
import io.gitlab.arturbosch.quide.api.core.ResourceAware

/**
 * @author Artur Bosch
 */
interface AnalysisContext : StorageAware, VersionAware, ResourceAware, PropertiesAware, FileSystem {
	val pluginId: String
	val fileSystem: FileSystem
	val quideDirectory: QuideDir
}
