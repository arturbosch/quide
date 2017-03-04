package io.gitlab.arturbosch.quide.shell

import io.gitlab.arturbosch.quide.shell.commands.RunQuide
import io.gitlab.arturbosch.quide.shell.commands.SetProject

/**
 * @author Artur Bosch
 */
object Commander {

	private val commands = mapOf(
			SetProject.id to SetProject,
			RunQuide.id to RunQuide
	)

	fun choose(line: String) {
		if (line.isNullOrBlank()) return
		commands.keys.find { line.startsWith(it) }
				?.let { commands[it]?.parse(line.substring(it.length))?.run() }
				?: throw QuideShellException("No matching command found!")
	}
}