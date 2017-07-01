package io.gitlab.arturbosch.quide.api.filesystem

import java.io.File

/**
 * @author Artur Bosch
 */
interface FileSystem {
	val projectDir: File
	val workDir: File

	fun inputFile(file: File): InputFile?
	fun inputFile(predicate: FilePredicate): InputFile?
	fun inputFiles(predicate: FilePredicate): Sequence<InputFile>
	fun files(predicate: FilePredicate): Sequence<File>

	fun inputDir(file: File): InputDir?
	fun resolvePath(path: String): File
}

typealias FilePredicate = (InputFile) -> Boolean