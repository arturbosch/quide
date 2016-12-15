package io.gitlab.arturbosch.quide.platform

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger


/**
 * @author Artur Bosch
 */
inline fun <reified T> loggerFor(): Logger {
	return LogManager.getLogger(T::class.javaClass.simpleName)
}