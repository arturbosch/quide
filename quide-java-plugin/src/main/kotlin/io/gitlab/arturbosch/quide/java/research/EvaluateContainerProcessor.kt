package io.gitlab.arturbosch.quide.java.research

import io.gitlab.arturbosch.quide.java.safeContainer
import io.gitlab.arturbosch.quide.platform.ControlFlow
import io.gitlab.arturbosch.quide.platform.Processor
import io.gitlab.arturbosch.quide.platform.UserData

/**
 * @author Artur Bosch
 */
class EvaluateContainerProcessor : Processor {

	override fun <U : UserData> execute(data: U) {
		val container = data.safeContainer().orElseThrow {
			InvalidContainerError("No container could be retrieved for evaluation!")
		}

		val result = evaluateToCSV(container)
		println(result)
	}

	override fun injectionPoint(): ControlFlow.InjectionPoint {
		return ControlFlow.InjectionPoint.AfterAnalysis
	}

	override fun priority(): Int {
		return 1000
	}

}

class InvalidContainerError(msg: String) : RuntimeException(msg)
