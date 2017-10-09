package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.jpal.ast.source.SourcePath
import io.gitlab.arturbosch.jpal.ast.source.SourceRange
import io.gitlab.arturbosch.quide.java.core.JavaCodeSmell
import io.gitlab.arturbosch.quide.java.mapping.ASTDiffTool
import io.gitlab.arturbosch.smartsmells.config.Smell
import io.gitlab.arturbosch.smartsmells.smells.CycleSpecific
import io.gitlab.arturbosch.smartsmells.smells.cycle.Cycle
import io.gitlab.arturbosch.smartsmells.smells.cycle.Dependency
import org.junit.Test
import java.io.File

/**
 * @author Artur Bosch
 */
class PatchCycleTest {

	val file1 = File(javaClass.getResource("/patch/CycleBase.java").path)
	val cycle = Cycle(
			Dependency("CycleTwo", "CycleTwo cycleTwo;", SourcePath.of(file1.toPath(), file1.toPath()), SourceRange.of(4, 4, 9, 27)),
			Dependency("CycleOne", "CycleOne cycleOne;", SourcePath.of(file1.toPath(), file1.toPath()), SourceRange.of(8, 8, 9, 27))
	)

	@Test
	fun patchRenamedCycleOne() {
		val file2 = File(javaClass.getResource("/patch/CycleOneMovedAndRenamed.java").path)
		val smell = patch(file1, file2, cycle)
		assert(smell.secondSignature() == "CycleOneRenamed cycleOne;")
	}

	private fun patch(file1: File, file2: File, cycle: CycleSpecific): CycleSpecific {
		val patch = ASTDiffTool().createPatchFor(MappingTest.SFile(file1), MappingTest.SFile(file2))
		val patched = patch.patchSmell(JavaCodeSmell(Smell.CYCLE, cycle))
		return patched.smell as Cycle
	}
}