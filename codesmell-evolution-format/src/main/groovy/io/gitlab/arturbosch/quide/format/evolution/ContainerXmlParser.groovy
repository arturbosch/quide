package io.gitlab.arturbosch.quide.format.evolution

import groovy.xml.MarkupBuilder
import io.gitlab.arturbosch.quide.model.CodeSmell
import io.gitlab.arturbosch.quide.model.SmellContainer
import io.gitlab.arturbosch.quide.validation.Validate
import io.gitlab.arturbosch.quide.vcs.Versionable
import org.apache.logging.log4j.core.util.StringBuilderWriter
import org.codehaus.groovy.runtime.IOGroovyMethods

/**
 * @author Artur Bosch
 */
class ContainerXmlParser implements CodeSmellContainerParser {

	private CodeSmellParser codeSmellParser

	ContainerXmlParser(CodeSmellParser codeSmellParser) {
		this.codeSmellParser = codeSmellParser
	}

	static <T extends CodeSmell> CodeSmellContainerParser create(SpecificCodeSmellParser<T> parser) {
		Validate.notNull(parser)
		return new ContainerXmlParser(new CodeSmellXmlParser(parser))
	}

	@Override
	String toXml(Versionable versionable, SmellContainer<CodeSmell> container) {
		def writer = new StringBuilderWriter()
		toXmlInternal(writer, versionable, container)
		return writer.toString()
	}

	private void toXmlInternal(Writer writer, Versionable versionable, SmellContainer<CodeSmell> container) {
		Validate.notNull(versionable)
		Validate.notNull(container)

		def v = versionable
		def mb = new MarkupBuilder(writer)
		mb.escapeAttributes = true

		mb.Quide('timestamp': new Date().toString()) {
			mb.Version('id': v.versionNumber(), 'hash': v.versionHash(), 'parentHash': v.parentHash()) {
				container.all().each {
					codeSmellParser.toXml(it, mb)
				}
			}
		}
	}

	@Override
	void toXmlFile(File file, Versionable versionable, SmellContainer<CodeSmell> container) {
		IOGroovyMethods.withCloseable(new PrintWriter(file)) {
			toXmlInternal(it, versionable, container)
		}
	}
}
