package io.gitlab.arturbosch.quide.core.fs

import io.gitlab.arturbosch.quide.api.fs.InputFile
import java.io.File
import java.io.InputStream
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
class DefaultInputFile(base: File,
					   override val file: File) : InputFile {

	override val ending: String = file.extension
	override val content: String by lazy { file.readText() }
	override val relativePath: String = file.toRelativeString(base)
	override val absolutePath: String = file.absolutePath
	override val path: Path by lazy(LazyThreadSafetyMode.NONE) { file.toPath() }
	override fun stream(): InputStream = file.inputStream()
}
