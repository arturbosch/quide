package io.gitlab.arturbosch.quide.api.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KProperty

/**
 * Provides logging through a delegate.
 */
fun logFactory() = LoggerDelegate()

class LoggerDelegate {

	private var logger: Logger? = null

	operator fun getValue(thisRef: Any, property: KProperty<*>): Logger {
		if (logger == null) logger = LoggerFactory.getLogger(thisRef.javaClass.simpleName)
		return logger!!
	}

}

open class LoggingProvider {
	var log: Logger = LoggerFactory.getLogger(javaClass.simpleName)
}
