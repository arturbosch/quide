package io.gitlab.arturbosch.quide.api.filesystem

import java.io.File
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
interface InputPath {
	val relativePath: String
	val absolutePath: String
	val file: File
	val path: Path
	fun isFile(): Boolean
	fun isDir(): Boolean
}