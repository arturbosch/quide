package io.gitlab.arturbosch.quide.format.evolution

import io.gitlab.arturbosch.quide.model.CodeSmell
import io.gitlab.arturbosch.quide.model.SmellContainer

/**
 * @author Artur Bosch
 */
interface CodeSmellContainerParser {

	String to(SmellContainer<CodeSmell> container)

	SmellContainer<CodeSmell> from(String container)

}