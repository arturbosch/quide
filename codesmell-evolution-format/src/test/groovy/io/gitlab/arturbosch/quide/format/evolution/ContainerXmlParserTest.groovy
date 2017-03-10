package io.gitlab.arturbosch.quide.format.evolution

import groovy.transform.Canonical
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
		def parser = new ContainerXmlParser(new CodeSmellXmlParser(new TestSpecificParser()))

		when:
		parser.to(version, container)
		parser.to(version2, container)

		then:
		true


	}

	private container() {

	}
}

class TestSpecificParser implements SpecificCodeSmellParser<TestSmell> {

	@Override
	String to(TestSmell smell) {
		return "nope"
	}

	@Override
	TestSmell from(String smell) {
		return null
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
		return null
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

	@Override
	List<TestSmell> all() {
		return [new TestSmell("LongMethod", "Artur"), new TestSmell("GodClass", "Artur"), new TestSmell("DeadCode", "Artur")]
	}

}

@Canonical
class TestSmell extends BaseCodeSmell {
	String name
	String author
}