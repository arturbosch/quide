package io.gitlab.arturbosch.quide.format.evolution

import groovy.xml.MarkupBuilder
import io.gitlab.arturbosch.quide.model.CodeSmell
import io.gitlab.arturbosch.quide.validation.Validate

/**
 * @author Artur Bosch
 */
class CodeSmellXmlParser implements CodeSmellParser {

	private SpecificCodeSmellParser parser

	CodeSmellXmlParser(SpecificCodeSmellParser parser) {
		this.parser = parser
	}

	@Override
	toXml(CodeSmell smell, MarkupBuilder mb) {
		Validate.notNull(smell)
		Validate.notNull(mb)

		mb.VersionedCodeSmell('start': smell.startVersion().versionNumber(),
				'end': smell.endVersion().versionNumber(),
				'alive': smell.alive,
				'consistent': smell.consistent,
				'weight': smell.weight(),
				'path': smell.sourcePath(),
				'killed': smell.killedInVersions().keySet(),
				'revived': smell.revivedInVersions().keySet(),
				'relocations': smell.relocations()) {
			parser.toXml(smell, mb)
		}
	}

}
