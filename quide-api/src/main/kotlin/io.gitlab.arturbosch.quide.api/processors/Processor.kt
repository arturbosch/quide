package io.gitlab.arturbosch.quide.api.processors

import io.gitlab.arturbosch.quide.api.Executable
import io.gitlab.arturbosch.quide.api.Nameable

/**
 * @author Artur Bosch
 */
interface Processor : Executable, Nameable {
	fun injectionPoint(): InjectionPoint = InjectionPoint.AfterAnalysis
	fun priority(): Int = 0
}
