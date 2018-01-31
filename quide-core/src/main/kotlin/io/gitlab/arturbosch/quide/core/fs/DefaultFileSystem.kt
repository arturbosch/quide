package io.gitlab.arturbosch.quide.core.fs

import io.gitlab.arturbosch.quide.api.fs.FilePredicate
import io.gitlab.arturbosch.quide.api.fs.FileSystem
import io.gitlab.arturbosch.quide.api.fs.InputDir
import io.gitlab.arturbosch.quide.api.fs.InputFile
import io.gitlab.arturbosch.quide.api.fs.InputPath
import java.io.File

/**
 * @author Artur Bosch
 */
open class DefaultFileSystem(
		override val projectDir: File,
		override val workDir: File) : FileSystem {

	private val allFiles by lazy {
		projectDir.walkTopDown()
				.map(::toFileOrDir)
				.filterNotNull()
				.toSet()
				.asSequence()
	}

	private fun toFileOrDir(it: File): InputPath? = when {
		it.isDirectory -> DefaultInputDir(projectDir, it)
		it.isFile -> DefaultInputFile(projectDir, it)
		else -> null
	}

	override fun inputFile(file: File): InputFile? = inputFiles { it.file == file }.firstOrNull()

	override fun inputFile(predicate: (InputFile) -> Boolean): InputFile? = inputFiles(predicate)
			.firstOrNull()

	override fun inputFiles(predicate: FilePredicate): Sequence<InputFile> = allFiles
			.filter { it.isFile() }
			.map { it as InputFile }
			.filter { predicate.invoke(it) }

	override fun files(predicate: FilePredicate): Sequence<File> = inputFiles(predicate)
			.map { it.file }

	override fun inputDir(file: File): InputDir? = allFiles
			.filter { it.isDir() }
			.filter { it.file == file }
			.firstOrNull() as InputDir

	override fun resolvePath(path: String): File = projectDir.resolve(path)
}
