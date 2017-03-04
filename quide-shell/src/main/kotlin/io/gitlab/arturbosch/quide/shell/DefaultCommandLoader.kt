package io.gitlab.arturbosch.quide.shell

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