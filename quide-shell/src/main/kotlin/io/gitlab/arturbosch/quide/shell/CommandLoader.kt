package io.gitlab.arturbosch.quide.shell

/**
 * @author Artur Bosch
 */
interface CommandLoader {
	fun load(): Map<String, Command>
}