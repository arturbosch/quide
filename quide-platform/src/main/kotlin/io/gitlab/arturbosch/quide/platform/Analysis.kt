package io.gitlab.arturbosch.quide.platform

import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Artur Bosch
 */
interface Analysis {
	val projectPath: Path
	val outputPath: Path?
	companion object {
		fun parse(args: Array<String>) = CliAnalysis(args)
	}
}

class CliAnalysis(args: Array<String>) : Analysis {

	override val projectPath: Path
	override val outputPath: Path?

	init {
		if (args.isEmpty()) throw IllegalArgumentException(
				"To start quide specify the project path to analyze. Optionally you can specify the output path.")
		projectPath = Paths.get(args[0])
		outputPath = if (args.size == 2) Paths.get(args[1]) else null
	}

}
