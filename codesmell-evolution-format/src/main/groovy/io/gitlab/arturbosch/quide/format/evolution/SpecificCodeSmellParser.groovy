package io.gitlab.arturbosch.quide.format.evolution

import io.gitlab.arturbosch.quide.model.CodeSmell

/**
 * @author Artur Bosch
 */
interface SpecificCodeSmellParser<T extends CodeSmell> {

	String to(T smell)

	T from(String smell)

}