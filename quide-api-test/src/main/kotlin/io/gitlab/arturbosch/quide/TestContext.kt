package io.gitlab.arturbosch.quide

import io.gitlab.arturbosch.quide.api.Plugin
import io.gitlab.arturbosch.quide.core.QuideDirectory
import io.gitlab.arturbosch.quide.core.Storage
import io.gitlab.arturbosch.quide.core.context.DefaultAnalysisContext

/**
 * @author Artur Bosch
 */
class TestContext(plugin: Plugin) : DefaultAnalysisContext(
		plugin.id, TestStorage, TestFileSystem, TestQuideDir)

object TestQuideDir : QuideDirectory(homeDir)
object TestStorage : Storage()
