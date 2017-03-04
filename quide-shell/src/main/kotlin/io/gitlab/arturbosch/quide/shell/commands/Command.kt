package io.gitlab.arturbosch.quide.shell.commands

/**
 * @author Artur Bosch
 */
interface Command {
	val id: String
	fun run(line: String): String
}