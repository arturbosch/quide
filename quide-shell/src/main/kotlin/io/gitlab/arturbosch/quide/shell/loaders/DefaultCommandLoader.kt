package io.gitlab.arturbosch.quide.shell.loaders

import io.gitlab.arturbosch.quide.shell.Command
import java.util.ServiceLoader

/**
 * @author Artur Bosch
 */
object DefaultCommandLoader : CommandLoader {

	override fun load(): Map<String, Command> {
		return ServiceLoader.load(Command::class.java, javaClass.classLoader)
				.asIterable().map { it.id to it }.toMap()
	}

}