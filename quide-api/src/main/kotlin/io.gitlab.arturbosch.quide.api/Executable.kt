package io.gitlab.arturbosch.quide.api

/**
 * @author Artur Bosch
 */
interface Executable<out R> {
	fun execute(context: AnalysisContext): R
}
