package io.gitlab.arturbosch.quide.java.format

import io.gitlab.arturbosch.quide.format.evolution.ContainerXmlParser
import io.gitlab.arturbosch.quide.java.lint
import io.gitlab.arturbosch.quide.vcs.FileChange
import io.gitlab.arturbosch.quide.vcs.Revision
import io.gitlab.arturbosch.quide.vcs.Versionable
import org.junit.Test
import java.time.ZonedDateTime
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