package io.gitlab.arturbosch.quide.api.processors

import io.gitlab.arturbosch.quide.api.Executable

/**
 * @author Artur Bosch
 */
interface Processor : Executable {
	fun injectionPoint(): InjectionPoint = InjectionPoint.AfterAnalysis
	fun priority(): Int = 0
}
