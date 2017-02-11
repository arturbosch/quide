package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.jpal.ast.source.SourcePath
import io.gitlab.arturbosch.jpal.ast.source.SourceRange
import io.gitlab.arturbosch.quide.java.core.JavaCodeSmell
import io.gitlab.arturbosch.quide.java.mapping.ASTDiffTool
import io.gitlab.arturbosch.smartsmells.config.Smell
import io.gitlab.arturbosch.smartsmells.smells.ElementTarget
import io.gitlab.arturbosch.smartsmells.smells.longmethod.LongMethod
import org.junit.Test
import java.io.File


/**
 * @author Artur Bosch
 */
class PatchMethodTest {

	val file1 = File(javaClass.getResource("/patch/Base.java").path)
	val longMethod = LongMethod("method", "public void method()", 1, 1,
			SourceRange.of(5, 5, 7, 6), SourcePath.of(file1.toPath(), file1.toPath()), ElementTarget.METHOD)

	@Test
	fun patchMethodSignatureChanged() {
		val file2 = File(javaClass.getResource("/patch/MethodSignatureChange.java").path)

		val smell = patch(file1, file2, longMethod)

		assert(smell.signature == "private String method(int i)")
	}

	@Test
	fun patchMethodOverloaded() {
		val file2 = File(javaClass.getResource("/patch/MethodOverloaded.java").path)

		val smell = patch(file1, file2, longMethod)

		assert(smell.signature == "private String method()")
	}

	@Test
	fun patchMethodMoved() {
		val file2 = File(javaClass.getResource("/patch/MethodMoved.java").path)

		val smell = patch(file1, file2, longMethod)

		assert(smell.sourceRange.toString() == "SourceRange(7, 9, 2, 2)")
	}

	@Test
	fun patchMethodMovedOfDeletion() {
		val file2 = File(javaClass.getResource("/patch/DeletionBeforeMethod.java").path)

		val smell = patch(file1, file2, longMethod)

		assert(smell.sourceRange.toString() == "SourceRange(2, 3, 2, 2)")
	}

	@Test
	fun patchMethodGrowthAndRenamed() {
		val file2 = File(javaClass.getResource("/patch/MethodGrowthPlusRename.java").path)

		val smell = patch(file1, file2, longMethod)

		assert(smell.sourceRange.toString() == "SourceRange(9, 24, 2, 2)")
	}

	private fun patch(file1: File, file2: File, longMethod: LongMethod): LongMethod {
		val patch = ASTDiffTool().createPatchFor(MappingTest.SFile(file1), MappingTest.SFile(file2))
		val patched = patch.patchSmell(JavaCodeSmell(Smell.LONG_METHOD, longMethod))
		return patched.smell as LongMethod
	}
}