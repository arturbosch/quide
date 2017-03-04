package io.gitlab.arturbosch.quide.shell.loaders

import io.gitlab.arturbosch.quide.shell.Command

/**
 * @author Artur Bosch
 */
interface CommandLoader {
	fun load(): Map<String, Command>
}