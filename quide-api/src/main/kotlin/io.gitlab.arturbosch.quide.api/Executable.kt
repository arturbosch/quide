package io.gitlab.arturbosch.quide.api

/**
 * @author Artur Bosch
 */
interface Executable {
	fun execute(context: AnalysisContext)
}
