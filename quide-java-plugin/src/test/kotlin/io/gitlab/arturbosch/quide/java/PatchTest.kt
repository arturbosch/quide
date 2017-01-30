package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.quide.java.mapping.ASTDiffTool
import org.junit.Ignore
import org.junit.Test
import java.io.File

/**
 * @author Artur Bosch
 */
class PatchTest {

	@Test
	@Ignore
	fun test() {
		val file1 = File("/home/artur/Repos/quide/quide-java-plugin/src/test/resources/Test1.java")
		val file2 = File("/home/artur/Repos/quide/quide-java-plugin/src/test/resources/Test2.java")

		val patch = ASTDiffTool().createPatchFor(MappingTest.SFile(file1), MappingTest.SFile(file2))

	}
}