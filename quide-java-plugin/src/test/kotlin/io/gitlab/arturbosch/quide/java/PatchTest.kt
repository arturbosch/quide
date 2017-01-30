package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.jpal.ast.source.SourcePath
import io.gitlab.arturbosch.jpal.ast.source.SourceRange
import io.gitlab.arturbosch.quide.java.mapping.ASTDiffTool
import io.gitlab.arturbosch.smartsmells.config.Smell
import io.gitlab.arturbosch.smartsmells.smells.longmethod.LongMethod
import org.junit.Test
import java.io.File

/**
 * @author Artur Bosch
 */
class PatchTest {

	@Test
	fun patchMethodSignatureChanged() {
		val file1 = File(javaClass.getResource("/patch/Base.java").path)
		val file2 = File(javaClass.getResource("/patch/MethodSignatureChange.java").path)

		val longMethod = LongMethod("method", "public void method()", 1, 1,
				SourceRange.of(5, 5, 7, 6), SourcePath.of(file1.toPath()))

		val smell = patch(file1, file2, longMethod)

		assert(smell.signature == "private String method(int i)")
	}

	@Test
	fun patchMethodOverloaded() {
		val file1 = File(javaClass.getResource("/patch/Base.java").path)
		val file2 = File(javaClass.getResource("/patch/MethodOverloaded.java").path)

		val longMethod = LongMethod("method", "public void method()", 1, 1,
				SourceRange.of(5, 5, 7, 6), SourcePath.of(file1.toPath()))

		val smell = patch(file1, file2, longMethod)
		println(smell.signature)
		assert(smell.signature == "private String method()")
	}

	private fun patch(file1: File, file2: File, longMethod: LongMethod): LongMethod {
		val patch = ASTDiffTool().createPatchFor(MappingTest.SFile(file1), MappingTest.SFile(file2))
		val patched = patch.patchSmell(JavaCodeSmell(Smell.LONG_METHOD, longMethod))
		return patched.smell as LongMethod
	}
}