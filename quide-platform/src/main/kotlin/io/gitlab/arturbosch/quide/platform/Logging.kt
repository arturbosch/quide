package io.gitlab.arturbosch.quide.platform

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KProperty


/**
 * @author Artur Bosch
 */
fun logFactory() = LoggerDelegate()

class LoggerDelegate {

	private var logger: Logger? = null

	operator fun getValue(thisRef: Any, property: KProperty<*>): Logger {
		if (logger == null) logger = LoggerFactory.getLogger(thisRef.javaClass.simpleName)
		return logger!!
	}

}