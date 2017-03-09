package io.gitlab.arturbosch.quide.format.evolution

import io.gitlab.arturbosch.quide.model.CodeSmell

/**
 * @author Artur Bosch
 */
class CodeSmellXmlParser implements CodeSmellParser {

	private SpecificCodeSmellParser parser

	CodeSmellXmlParser(SpecificCodeSmellParser parser) {
		this.parser = parser
	}

	@Override
	String to(CodeSmell smell) {
		return null
	}

	@Override
	CodeSmell from(String smell) {
		return null
	}
}
