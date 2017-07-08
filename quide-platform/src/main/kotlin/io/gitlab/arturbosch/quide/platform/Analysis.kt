package io.gitlab.arturbosch.quide.platform

import java.nio.file.Path
import java.util.Optional

/**
 * @author Artur Bosch
 */
interface Analysis : AnalysisContext {
	companion object {
		fun parse(args: Array<String>) = CliAnalysis(args)
	}
}

class CliAnalysis(args: Array<String>) : Analysis {

	private val projectPath: Path
	private val outputPath: Optional<Path>

	init {
		val arguments = parseArguments(args)
		projectPath = arguments.input!!
		outputPath = Optional.ofNullable(arguments.output)
	}

	override fun projectPath(): Path = projectPath

	override fun outputPath(): Optional<Path> = outputPath

	override fun quideDirectory(): QuideDirectory = HomeFolder
}
