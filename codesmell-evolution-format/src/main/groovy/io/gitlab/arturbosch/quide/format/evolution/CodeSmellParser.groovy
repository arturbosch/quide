package io.gitlab.arturbosch.quide.format.evolution

import io.gitlab.arturbosch.quide.model.CodeSmell

/**
 * @author Artur Bosch
 */
interface CodeSmellParser {

	String to(CodeSmell smell)

	CodeSmell from(String smell)

}