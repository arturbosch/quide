package io.gitlab.arturbosch.quide.java.core

import io.gitlab.arturbosch.quide.java.JavaPluginData
import io.gitlab.arturbosch.quide.java.mapping.ASTMapping
import io.gitlab.arturbosch.quide.platform.ControlFlow
import io.gitlab.arturbosch.quide.platform.Processor
import io.gitlab.arturbosch.quide.platform.UserData

/**
 * @author Artur Bosch
 */
class MappingProcessor : Processor {

	private val mapping = ASTMapping()

	override fun <U : UserData> execute(data: U) {
		val pluginData = data as JavaPluginData
		if (pluginData.isEvolutionaryAnalysis()) {
			mapping.execute(data)
		}
	}

	override fun injectionPoint(): ControlFlow.InjectionPoint {
		return ControlFlow.InjectionPoint.AfterDetection
	}
}