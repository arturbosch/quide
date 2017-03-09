package io.gitlab.arturbosch.quide.format.evolution

import io.gitlab.arturbosch.quide.model.CodeSmell
import io.gitlab.arturbosch.quide.model.SmellContainer

/**
 * @author Artur Bosch
 */
class ContainerXmlParser implements CodeSmellContainerParser {

	private CodeSmellParser codeSmellParser

	ContainerXmlParser(CodeSmellParser codeSmellParser) {
		this.codeSmellParser = codeSmellParser
	}

	@Override
	String to(SmellContainer<CodeSmell> container) {
		return null
	}

	@Override
	SmellContainer<CodeSmell> from(String container) {
		return null
	}
}
