package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.quide.java.mapping.ASTDiffTool
import org.junit.Test
import java.io.File

/**
 * @author Artur Bosch
 */
class PatchTest {

	@Test
	fun test() {
		val file1 = File("/home/artur/Repos/quide/quide-java-plugin/src/test/resources/Version5.java")
		val file2 = File("/home/artur/Repos/quide/quide-java-plugin/src/test/resources/Version6.java")

		val patch = ASTDiffTool().createPatchFor(MappingTest.SFile(file1), MappingTest.SFile(file2))

	}
}