package io.gitlab.arturbosch.quide.shell

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException
import io.gitlab.arturbosch.quide.platform.cli.ExistingPathConverter
import java.nio.file.Files
import java.nio.file.Path

/**
 * @author Artur Bosch
 */

object ShellOpts {
	@Parameter(names = ["--project", "-p"],
			description = "Specify a project quide should operate on 'run'.",
			converter = ExistingPathConverter::class)
	var input: Path? = null
	@Parameter(names = ["--help", "-h"], help = true, description = "Prints the help message.")
	var help = false
}

private val jCommander = JCommander()

fun parseArguments(args: Array<String>) {
	jCommander.programName = "quide-shell"
	jCommander.addObject(ShellOpts)

	try {
		jCommander.parse(*args)
	} catch (ex: ParameterException) {
		println(ex.message)
		println()
		jCommander.usage()
		System.exit(-1)
	}

	if (ShellOpts.help) {
		jCommander.usage()
		System.exit(-1)
	}

	val project = ShellOpts.input
	if (isValidProjectPath(project)) {
		QuideState.projectPath = project
	}
}

private fun isValidProjectPath(project: Path?) = project != null && Files.isDirectory(project)
