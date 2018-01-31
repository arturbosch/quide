package io.gitlab.arturbosch.quide.core.fs

import io.gitlab.arturbosch.quide.api.fs.InputDir
import java.io.File
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
class DefaultInputDir(base: File,
					  override val file: File) : InputDir {

	override val relativePath: String = file.toRelativeString(base)
	override val absolutePath: String = file.absolutePath
	override val path: Path by lazy(LazyThreadSafetyMode.NONE) { file.toPath() }
}
