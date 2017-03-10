package io.gitlab.arturbosch.quide.format.evolution

import io.gitlab.arturbosch.quide.model.CodeSmell
import io.gitlab.arturbosch.quide.model.SmellContainer
import io.gitlab.arturbosch.quide.vcs.Versionable

/**
 * @author Artur Bosch
 */
interface CodeSmellContainerParser {

	String toXml(Versionable versionable, SmellContainer<CodeSmell> container)

}