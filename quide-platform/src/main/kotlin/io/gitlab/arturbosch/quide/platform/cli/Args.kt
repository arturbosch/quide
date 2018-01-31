package io.gitlab.arturbosch.quide.platform.cli

import com.beust.jcommander.Parameter
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
class Args {
	@Parameter(names = ["--input", "-i"],
			required = true,
			description = "The input project path.",
			converter = ExistingPathConverter::class)
	private var _input: Path? = null
	val input: Path get() = _input ?: throw JcommanderInitializationFailure()
	@Parameter(names = ["--output", "-o"],
			description = "The output report folder path.",
			converter = DirectoryPathConverter::class)
	var output: Path? = null
	@Parameter(names = ["--properties", "-p"],
			description = "Additional properties as key=value pairs.")
	var properties: String? = null
	@Parameter(names = ["--propertyPaths", "-pp"],
			description = "Additional property paths separated by comma's.")
	var propertyPaths: String? = null
	@Parameter(names = ["--help", "-h"],
			help = true,
			description = "Prints the help message.")
	var help = false
}
