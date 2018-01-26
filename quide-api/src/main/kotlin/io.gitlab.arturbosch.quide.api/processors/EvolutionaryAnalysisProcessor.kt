package io.gitlab.arturbosch.quide.api.processors

import io.gitlab.arturbosch.quide.api.AnalysisContext

/**
 * @author Artur Bosch
 */
interface EvolutionaryAnalysisProcessor : ConditionalProcessor {

	override fun isActive(context: AnalysisContext): Boolean = context.isEvolutionaryAnalysis
}
