package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.jpal.ast.source.SourcePath
import io.gitlab.arturbosch.jpal.ast.source.SourceRange
import io.gitlab.arturbosch.quide.java.mapping.ASTDiffTool
import io.gitlab.arturbosch.smartsmells.config.Smell
import io.gitlab.arturbosch.smartsmells.smells.ClassSpecific
import io.gitlab.arturbosch.smartsmells.smells.largeclass.LargeClass
import org.junit.Test
import java.io.File

/**
 * @author Artur Bosch
 */
class PatchClassTest {

	val file1 = File(javaClass.getResource("/patch/Base.java").path)
	val largeClass = LargeClass("Test", "Test", 1, 1,
			SourcePath.of(file1.toPath()), SourceRange.of(1, 8, 1, 2))

	@Test
	fun patchRenamedClass() {
		val file2 = File(javaClass.getResource("/patch/ClassRenamed.java").path)

		val smell = patch(file1, file2, largeClass)

		assert(smell.signature() == "Renamed<T extends Object>")
	}

	@Test
	fun patchMovedClassByImports() {
		val file2 = File(javaClass.getResource("/patch/MovedClassOfImports.java").path)

		val smell = patch(file1, file2, largeClass)

		assert(smell.positions.toString() == "SourceRange(7, 14, 1, 1)")
	}

	@Test
	fun patchMovedClassByImportsAndRenamed() {
		val file2 = File(javaClass.getResource("/patch/MovedClassOfImportsAndRenamed.java").path)

		val smell = patch(file1, file2, largeClass)

		assert(smell.positions.toString() == "SourceRange(7, 14, 1, 1)")
	}

	@Test
	fun patchGenericTypesOfInnerClass() {
		val file1 = File(javaClass.getResource("/patch/InnerClassesBase.java").path)
		val file2 = File(javaClass.getResource("/patch/GenericTypesInnerClassChanged.java").path)

		val largeClass = LargeClass("InnerTest", "Test\$InnerTest<T extends String, B extends Boolean>", 1, 1,
				SourcePath.of(file1.toPath()), SourceRange.of(9, 15, 2, 2))
		val smell = patch(file1, file2, largeClass)

		assert(smell.signature() == "Test\$InnerTest<T extends Integer, B extends Integer>")
	}

	private fun patch(file1: File, file2: File, largeClass: ClassSpecific): ClassSpecific {
		val patch = ASTDiffTool().createPatchFor(MappingTest.SFile(file1), MappingTest.SFile(file2))
		val patched = patch.patchSmell(JavaCodeSmell(Smell.LARGE_CLASS, largeClass))
		return patched.smell as LargeClass
	}
}