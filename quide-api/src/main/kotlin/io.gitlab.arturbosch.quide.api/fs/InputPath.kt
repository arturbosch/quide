package io.gitlab.arturbosch.quide.api.fs

import java.io.File
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
interface InputPath {
	val relativePath: String
	val absolutePath: String
	val name get() = file.nameWithoutExtension
	val file: File
	val path: Path
	fun isFile(): Boolean = false
	fun isDir(): Boolean = false
}
