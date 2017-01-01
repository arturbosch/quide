package io.gitlab.arturbosch.quide.platform

import java.nio.file.Path
import java.nio.file.Paths
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
	private val outputPath: Path?

	init {
		if (args.isEmpty()) throw IllegalArgumentException(
				"To start quide specify the project path to analyze. Optionally you can specify the output path.")
		projectPath = Paths.get(args[0])
		outputPath = if (args.size == 2) Paths.get(args[1]) else null
	}

	override fun projectPath(): Path = projectPath

	override fun outputPath(): Optional<Path> = Optional.ofNullable(outputPath)

	override fun quideDirectory(): QuideDirectory = HomeFolder
}
