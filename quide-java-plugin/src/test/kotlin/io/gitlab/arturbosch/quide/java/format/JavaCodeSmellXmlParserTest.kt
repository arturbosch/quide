package io.gitlab.arturbosch.quide.java.format

import groovy.xml.MarkupBuilder
import io.gitlab.arturbosch.jpal.ast.source.SourcePath
import io.gitlab.arturbosch.jpal.ast.source.SourceRange
import io.gitlab.arturbosch.quide.format.evolution.ContainerXmlParser
import io.gitlab.arturbosch.quide.java.core.JavaCodeSmell
import io.gitlab.arturbosch.quide.java.lint
import io.gitlab.arturbosch.quide.vcs.FileChange
import io.gitlab.arturbosch.quide.vcs.Revision
import io.gitlab.arturbosch.quide.vcs.Versionable
import io.gitlab.arturbosch.smartsmells.config.Smell
import io.gitlab.arturbosch.smartsmells.smells.ElementTarget
import io.gitlab.arturbosch.smartsmells.smells.statechecking.StateChecking
import org.apache.logging.log4j.core.util.StringBuilderWriter
import org.junit.Test
import java.time.ZonedDateTime
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * @author Artur Bosch
 */
class JavaCodeSmellXmlParserTest {

	@Test
	fun parseJavaCodeSmellsToXml() {
		val container = "repo/version1/Version.java".lint()
		val version = TestVersion(1)
		container.all().forEach { it.applyVersion(version) }

		val parser = ContainerXmlParser.create(JavaCodeSmellXmlParser())
		val xml = parser.toXml(version, container)

		assertTrue { xml.contains("smellType='CommentSmell") }
	}

	@Test
	fun staticAttributesAreNotParsed() {
		val parser = JavaCodeSmellXmlParser()
		val writer = StringBuilderWriter()
		val builder = MarkupBuilder(writer)
		val smell = StateChecking("Scope", listOf("case"), StateChecking.getSUBTYPING(),
				SourcePath("hello", "hello"), SourceRange.of(1, 1, 1, 1), ElementTarget.ANY)

		parser.toXml(JavaCodeSmell(Smell.STATE_CHECKING, smell), builder)
		val xml = writer.toString()

		assertFalse { xml.contains("INSTANCE_OF") }
		assertFalse { xml.contains("SUBTYPING") }
	}

}

data class TestVersion(val number: Int) : Versionable {
	override fun versionNumber(): Int = number
	override fun revision() = object : Revision {
		override fun versionHash(): String = "123456"
		override fun parentHash(): String = "654321"
		override fun message(): String = ""
		override fun author(): String = ""
		override fun date(): ZonedDateTime = ZonedDateTime.now()
		override fun isMerge(): Boolean = false
	}

	override fun fileChanges(): MutableList<FileChange> = mutableListOf()
}