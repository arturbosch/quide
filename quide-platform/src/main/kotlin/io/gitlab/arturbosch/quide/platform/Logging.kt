package io.gitlab.arturbosch.quide.platform

import java.util.logging.Logger

/**
 * @author Artur Bosch
 */
inline fun <reified T> loggerFor(): Logger {
	return Logger.getLogger(T::class.javaClass.simpleName)
}