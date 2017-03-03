package io.gitlab.arturbosch.quide.shell.commands

/**
 * @author Artur Bosch
 */
interface Parsable {
	val id: String
	fun parse(line: String): Commando
}