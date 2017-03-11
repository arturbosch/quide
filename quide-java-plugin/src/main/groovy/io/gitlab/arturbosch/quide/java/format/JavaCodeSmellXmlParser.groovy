package io.gitlab.arturbosch.quide.java.format

import groovy.xml.MarkupBuilder
import io.gitlab.arturbosch.quide.model.BaseCodeSmell
import io.gitlab.arturbosch.smartsmells.smells.DetectionResult

/**
 * @author Artur Bosch
 */
class JavaCodeSmellXmlParser implements BaseCodeSmellParserExtension {

	@Override
	void toXml(BaseCodeSmell smell, MarkupBuilder mb) {
		DetectionResult result = reflectionHack(smell)
		def name = result.class.simpleName
		def attributes = toAttributeMap(result)
		mb.JavaCodeSmell('smellType': name) {
			mb.CodeSmellInfo(attributes)
		}
	}

	private static DetectionResult reflectionHack(BaseCodeSmell smell) {
		def field = smell.class.getDeclaredField("smell")
		field.setAccessible(true)
		return field.get(smell) as DetectionResult
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
