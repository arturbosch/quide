package io.gitlab.arturbosch.quide.format.evolution

import groovy.xml.MarkupBuilder
import io.gitlab.arturbosch.quide.model.CodeSmell
import io.gitlab.arturbosch.quide.model.SmellContainer
import io.gitlab.arturbosch.quide.vcs.Versionable

/**
 * @author Artur Bosch
 */
class ContainerXmlParser implements CodeSmellContainerParser {

	private CodeSmellParser codeSmellParser

	ContainerXmlParser(CodeSmellParser codeSmellParser) {
		this.codeSmellParser = codeSmellParser
	}

	@Override
	String toXml(Versionable versionable, SmellContainer<CodeSmell> container) {
		def mb = new MarkupBuilder()

		def xml = mb.quide('timestamp': new Date().toString()) {
			container.all().each {
				codeSmellParser.toXml(it, mb)
			}
		}
		""
	}

}
