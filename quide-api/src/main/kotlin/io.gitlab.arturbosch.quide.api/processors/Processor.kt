package io.gitlab.arturbosch.quide.api.processors

import io.gitlab.arturbosch.quide.api.AnalysisContext

/**
 * @author Artur Bosch
 */
interface Processor {
	fun execute(context: AnalysisContext)
}
