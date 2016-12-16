package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.quide.platform.ControlFlow
import io.gitlab.arturbosch.quide.platform.Processor
import io.gitlab.arturbosch.quide.platform.UserData
import org.apache.logging.log4j.LogManager

/**
 * @author Artur Bosch
 */
class PrintProcessor : Processor {

	private val logger = LogManager.getLogger(PrintProcessor::class.java.simpleName)

	override fun <U : UserData> execute(data: U) {
		val smells = data.currentContainer().orElseThrow {
			RuntimeException("Could not retrieve container!")
		}.all()
		logger.info("SmartSmells detected: #${smells.size} smells")
	}

	override fun injectionPoint(): ControlFlow.InjectionPoint {
		return ControlFlow.InjectionPoint.AfterAnalysis
	}
}