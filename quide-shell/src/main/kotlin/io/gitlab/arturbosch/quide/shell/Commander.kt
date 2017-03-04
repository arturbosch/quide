package io.gitlab.arturbosch.quide.shell

/**
 * @author Artur Bosch
 */
class Commander(loader: CommandLoader) {

	private val commands: Map<String, Command> = loader.load()

	fun choose(line: String) {
		if (line.isNullOrBlank()) return
		commands.keys.find { line.startsWith(it) }
				?.let { println(commands[it]?.run(line.substring(it.length))) }
				?: throw QuideShellException("No matching command found!")
	}
}