package io.gitlab.arturbosch.quide.java.format

import groovy.xml.MarkupBuilder
import io.gitlab.arturbosch.quide.format.evolution.SpecificCodeSmellParser
import io.gitlab.arturbosch.quide.java.core.JavaCodeSmell
import io.gitlab.arturbosch.smartsmells.smells.DetectionResult

/**
 * @author Artur Bosch
 */
class JavaCodeSmellXmlParser implements SpecificCodeSmellParser<JavaCodeSmell> {

	@Override
	toXml(JavaCodeSmell smell, MarkupBuilder mb) {
		def name = smell.smell.class.simpleName
		def attributes = toAttributeMap(smell.smell)
		mb.JavaCodeSmell('smellType': name) {
			mb.CodeSmellInfo(attributes)
		}
	}

	private static Map<String, String> toAttributeMap(DetectionResult smelly) {
		def positions = extractPositions(smelly)
		return smelly.class.declaredFields
				.grep { !it.synthetic }
				.grep { it.name != "sourceRange" }
				.grep { it.name != "sourcePath" }
				.collectEntries() {
			it.setAccessible(true)
			[it.name, it.get(smelly).toString()]
		} + positions
	}

	private static Map<String, String> extractPositions(DetectionResult smelly) {
		smelly.class.getDeclaredField("sourceRange").with {
			setAccessible(true)
			def pos = get(smelly).toString().replace("SourceRange(", "").replace(")", "").split(',').collect { it.trim() }
			["startLine": pos[0], "startColumn": pos[1], "endLine": pos[2], "endColumn": pos[3]]
		}
	}

}
