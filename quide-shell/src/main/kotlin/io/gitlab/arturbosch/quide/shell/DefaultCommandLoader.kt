package io.gitlab.arturbosch.quide.shell

import io.gitlab.arturbosch.quide.platform.logFactory
import java.util.ServiceLoader

/**
 * @author Artur Bosch
 */
object DefaultCommandLoader : CommandLoader {

	private val logger by logFactory()

	override fun load(): Map<String, Command> {
		return ServiceLoader.load(Command::class.java, javaClass.classLoader)
				.asIterable().map { it.id to it }.toMap().apply {
			logger.info("Loaded Commands: " + keys.joinToString(", "))
		}
	}

}