package io.gitlab.arturbosch.quide.api.filesystem

import java.io.File

/**
 * @author Artur Bosch
 */
interface FileSystem {
	val projectDir: File
	val workDir: File
	fun inputFile(predicate: FilePredicate): InputFile?
	fun inputFiles(predicate: FilePredicate): Sequence<InputFile>
	fun files(predicate: FilePredicate): Sequence<File>
}
