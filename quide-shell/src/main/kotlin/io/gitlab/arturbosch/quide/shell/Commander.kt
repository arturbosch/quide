package io.gitlab.arturbosch.quide.shell

import io.gitlab.arturbosch.quide.shell.commands.Command

/**
 * @author Artur Bosch
 */
object Commander {

	private val commands = mapOf<String, Command>()

	fun choose(line: String) {
		if (line.isNullOrBlank()) return
		commands.keys.find { line.startsWith(it) }
				?.let { commands[it]?.run(line.substring(it.length)) }
				?: throw QuideShellException("No matching command found!")
	}
}