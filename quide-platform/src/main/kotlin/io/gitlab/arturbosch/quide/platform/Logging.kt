package io.gitlab.arturbosch.quide.platform

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.reflect.KProperty


/**
 * @author Artur Bosch
 */
fun logFactory() = LoggerDelegate()

class LoggerDelegate {

	private var logger: Logger? = null

	operator fun getValue(thisRef: Any, property: KProperty<*>): Logger {
		if (logger == null) logger = LogManager.getLogger(thisRef.javaClass.simpleName)
		return logger!!
	}

}