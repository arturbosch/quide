package io.gitlab.arturbosch.quide.format.evolution

import groovy.xml.MarkupBuilder
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
	String toXml(CodeSmell smell, MarkupBuilder mb) {
		return null
	}

}
