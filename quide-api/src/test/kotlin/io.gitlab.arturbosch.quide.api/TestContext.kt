package io.gitlab.arturbosch.quide.api

import io.gitlab.arturbosch.quide.api.core.QuideDirectory
import io.gitlab.arturbosch.quide.api.core.Storage

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