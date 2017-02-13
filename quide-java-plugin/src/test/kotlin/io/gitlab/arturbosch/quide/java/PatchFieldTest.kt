package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.jpal.ast.source.SourcePath
import io.gitlab.arturbosch.jpal.ast.source.SourceRange
import io.gitlab.arturbosch.quide.java.core.JavaCodeSmell
import io.gitlab.arturbosch.quide.java.mapping.ASTDiffTool
import io.gitlab.arturbosch.smartsmells.config.Smell
import io.gitlab.arturbosch.smartsmells.smells.ElementTarget
import io.gitlab.arturbosch.smartsmells.smells.FieldSpecific
import io.gitlab.arturbosch.smartsmells.smells.deadcode.DeadCode
import org.junit.Test
import java.io.File

/**
 * @author Artur Bosch
 */
class PatchFieldTest {

	val file1 = File(javaClass.getResource("/patch/Base.java").path)
	val deadCode = DeadCode("value", "private int value;", SourcePath.of(file1.toPath(), file1.toPath()),
			SourceRange.of(3, 3, 5, 23), ElementTarget.FIELD)

	@Test
	fun patchRenamedField() {
		val file2 = File(javaClass.getResource("/patch/FieldRenamed.java").path)

		val smell = patch(file1, file2, deadCode)

		assert(smell.signature() == "private int myInt;")
	}

	@Test
	fun patchMovedField() {
		val file2 = File(javaClass.getResource("/patch/FieldMoved.java").path)

		val smell = patch(file1, file2, deadCode)

		assert(smell.signature() == "private int value;")
	}

	@Test
	fun patchUnnamedAndMovedField() {
		val file2 = File(javaClass.getResource("/patch/FieldRenamedAndMoved.java").path)

		val smell = patch(file1, file2, deadCode)

		assert(smell.signature() == "private int myInt;")
	}

	private fun patch(file1: File, file2: File, deadCode: FieldSpecific): FieldSpecific {
		val patch = ASTDiffTool().createPatchFor(MappingTest.SFile(file1), MappingTest.SFile(file2))
		val patched = patch.patchSmell(JavaCodeSmell(Smell.DEAD_CODE, deadCode))
		return patched.smell as DeadCode
	}
}