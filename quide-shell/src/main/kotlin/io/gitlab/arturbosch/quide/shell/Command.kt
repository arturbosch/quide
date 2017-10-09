package io.gitlab.arturbosch.quide.shell

/**
 * @author Artur Bosch
 */
interface Command {
	val id: String
	fun run(line: String): String
}