package io.gitlab.arturbosch.quide.shell

import io.gitlab.arturbosch.quide.shell.commands.Command

/**
 * @author Artur Bosch
 */
class Commander(loader: CommandLoader) {

	private val commands: Map<String, Command> = loader.load()

	fun choose(line: String) {
		if (line.isNullOrBlank()) return
		commands.keys.find { line.startsWith(it) }
				?.let { commands[it]?.run(line.substring(it.length)) }
				?: throw QuideShellException("No matching command found!")
	}
}