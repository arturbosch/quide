package io.gitlab.arturbosch.quide.api.filesystem

/**
 * @author Artur Bosch
 */
interface FilePredicate {
	fun apply(inputFile: InputFile)
}
