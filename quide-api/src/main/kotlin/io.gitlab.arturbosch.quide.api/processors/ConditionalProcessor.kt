package io.gitlab.arturbosch.quide.api.processors

import io.gitlab.arturbosch.quide.api.AnalysisContext

/**
 * @author Artur Bosch
 */
interface ConditionalProcessor : Processor {

	fun isActive(context: AnalysisContext): Boolean

	override fun execute(context: AnalysisContext) {
		if (isActive(context)) {
			onExecution(context)
		}
	}

	fun onExecution(context: AnalysisContext)

}