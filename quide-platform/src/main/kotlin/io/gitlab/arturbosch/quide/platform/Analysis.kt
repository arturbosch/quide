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

	private val projectPath: Path = Paths.get(args[0])
	private val outputPath: Path? = if (args.size == 2) Paths.get(args[1]) else null

	init {
		if (args.isEmpty()) throw IllegalArgumentException(
				"To start quide specify the project path to analyze. Optionally you can specify the output path.")
	}

	override fun projectPath(): Path = projectPath

	override fun outputPath(): Optional<Path> = Optional.ofNullable(outputPath)

	override fun quideDirectory(): QuideDirectory = HomeFolder
}
