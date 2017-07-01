package io.gitlab.arturbosch.quide.api

import io.gitlab.arturbosch.quide.api.filesystem.FilePredicate
import io.gitlab.arturbosch.quide.api.filesystem.FileSystem
import io.gitlab.arturbosch.quide.api.filesystem.InputDir
import io.gitlab.arturbosch.quide.api.filesystem.InputFile
import io.gitlab.arturbosch.quide.api.filesystem.InputPath
import io.gitlab.arturbosch.quide.api.utils.ResourceAware
import java.io.File
import java.io.InputStream
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
object TestFileSystem : FileSystem, ResourceAware {

	override val projectDir = File(resource("baseDir").toURI())
	override val workDir = File(resource("workDir").toURI())

	private val _allFiles = projectDir.walkTopDown().map {
		when {
			it.isDirectory -> TestInputDir(projectDir, it)
			it.isFile -> TestInputFile(projectDir, it)
			else -> null
		}
	}.filterNotNull().toList()

	private val allFiles: Sequence<InputPath> = _allFiles.asSequence()

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

class TestInputDir(base: File,
				   override val file: File) : InputDir {
	override val relativePath: String = file.toRelativeString(base)
	override val absolutePath: String = file.absolutePath
	override val path: Path by lazy(LazyThreadSafetyMode.NONE) { file.toPath() }
}

class TestInputFile(base: File,
					override val file: File) : InputFile {

	override val ending: String = file.extension
	override val content: String by lazy { file.readText() }
	override val relativePath: String = file.toRelativeString(base)
	override val absolutePath: String = file.absolutePath
	override val path: Path by lazy(LazyThreadSafetyMode.NONE) { file.toPath() }
	override fun stream(): InputStream = file.inputStream()
}