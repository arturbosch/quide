package io.gitlab.arturbosch.quide.format.evolution

import io.gitlab.arturbosch.quide.model.CodeSmell
import io.gitlab.arturbosch.quide.model.SmellContainer
import io.gitlab.arturbosch.quide.vcs.Versionable

/**
 * @author Artur Bosch
 */
interface CodeSmellContainerParser {

	def <T extends SmellContainer<? extends CodeSmell>> String toXml(Versionable versionable, T container)

	def <T extends SmellContainer<? extends CodeSmell>> void toXmlFile(File file, Versionable versionable, T container)
}