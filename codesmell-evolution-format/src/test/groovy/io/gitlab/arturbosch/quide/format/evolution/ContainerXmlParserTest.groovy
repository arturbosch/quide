package io.gitlab.arturbosch.quide.format.evolution

import groovy.transform.Canonical
import groovy.xml.MarkupBuilder
import io.gitlab.arturbosch.quide.model.BaseCodeSmell
import io.gitlab.arturbosch.quide.model.SmellContainer
import io.gitlab.arturbosch.quide.vcs.FileChange
import io.gitlab.arturbosch.quide.vcs.Revision
import io.gitlab.arturbosch.quide.vcs.Versionable
import spock.lang.Specification

import java.time.ZonedDateTime

/**
 * @author Artur Bosch
 */
class ContainerXmlParserTest extends Specification {

	def "container is parsed to xml"() {
		given:
		def container = new TestContainer()
		def version = new TestVersion(1)
		def version2 = new TestVersion(2)
		def parser = ContainerXmlParser.create(new TestSpecificParser())
		container.all().each { it.startVersion = version }
		container.all().each { it.endVersion = version2 }

		when:
		def xml = parser.toXml(version, container)
		def xml2 = parser.toXml(version2, container)

		then:
		xml.contains("Quide")
		xml.contains("Version")
		xml.contains("VersionedCodeSmell")
		xml.contains("CodeSmellInfo")
		xml2.contains("Quide")
		xml2.contains("Version")
		xml2.contains("VersionedCodeSmell")
		xml2.contains("CodeSmellInfo")
	}

}

class TestSpecificParser implements SpecificCodeSmellParser<TestSmell> {

	@Override
	toXml(TestSmell smell, MarkupBuilder mb) {
		mb.CodeSmellInfo('smellType': smell.name, 'author': smell.severity)
	}

}

class TestVersion implements Versionable {

	int id

	TestVersion(int id) {
		this.id = id
	}

	@Override
	int versionNumber() {
		return id
	}

	@Override
	Revision revision() {
		return new TestRevision()
	}

	@Override
	List<FileChange> fileChanges() {
		return null
	}
}

class TestRevision implements Revision {

	@Override
	String versionHash() {
		return "123456"
	}

	@Override
	String parentHash() {
		return "654321"
	}

	@Override
	String message() {
		return null
	}

	@Override
	String author() {
		return null
	}

	@Override
	ZonedDateTime date() {
		return null
	}

	@Override
	boolean isMerge() {
		return false
	}
}

class TestContainer implements SmellContainer<TestSmell> {
	private List<TestSmell> smells =
			[new TestSmell("LongMethod", "high"), new TestSmell("GodClass", "medium"), new TestSmell("DeadCode", "low")]

	@Override
	List<TestSmell> all() {
		return smells
	}

}

@Canonical
class TestSmell extends BaseCodeSmell {
	String name
	String severity

	@Override
	String sourcePath() {
		return "/this/that/path"
	}
}