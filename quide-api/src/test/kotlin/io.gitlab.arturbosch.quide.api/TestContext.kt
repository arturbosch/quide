package io.gitlab.arturbosch.quide.api

import io.gitlab.arturbosch.quide.api.core.QuideDirectory
import io.gitlab.arturbosch.quide.api.core.Storage
import io.gitlab.arturbosch.quide.api.filesystem.FilePredicate
import io.gitlab.arturbosch.quide.api.filesystem.FileSystem
import io.gitlab.arturbosch.quide.api.filesystem.InputFile
import java.io.File

/**
 * @author Artur Bosch
 */
class TestContext(
		plugin: Plugin,
		context: Plugin.Context)
	: AbstractAnalysisContext(
		plugin.id,
		TestStorage,
		TestFileSystem,
		TestQuideDir)

object TestQuideDir : QuideDirectory()
object TestStorage : Storage()
object TestFileSystem : FileSystem {
	override val projectDir: File
		get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
	override val workDir: File
		get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

	override fun inputFile(predicate: FilePredicate): InputFile? {
		throw UnsupportedOperationException("not implemented")
	}

	override fun inputFiles(predicate: FilePredicate): Sequence<InputFile> {
		throw UnsupportedOperationException("not implemented")
	}

	override fun files(predicate: FilePredicate): Sequence<File> {
		throw UnsupportedOperationException("not implemented")
	}
}