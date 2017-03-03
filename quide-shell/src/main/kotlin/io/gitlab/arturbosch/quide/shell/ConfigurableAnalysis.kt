package io.gitlab.arturbosch.quide.shell

import io.gitlab.arturbosch.quide.platform.Analysis
import io.gitlab.arturbosch.quide.platform.HomeFolder
import io.gitlab.arturbosch.quide.platform.QuideDirectory
import java.nio.file.Path
import java.util.Optional

/**
 * @author Artur Bosch
 */
object ConfigurableAnalysis : Analysis {

	override fun projectPath(): Path = QuideState.projectPath ?: throw QuideShellException("No project path specified, use 'project' " +
			"command first!")

	override fun outputPath(): Optional<Path> = Optional.empty()

	override fun quideDirectory(): QuideDirectory = HomeFolder
}