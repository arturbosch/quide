package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.jpal.ast.source.SourcePath
import io.gitlab.arturbosch.jpal.ast.source.SourceRange
import io.gitlab.arturbosch.quide.java.mapping.ASTDiffTool
import io.gitlab.arturbosch.smartsmells.config.Smell
import io.gitlab.arturbosch.smartsmells.smells.CycleSpecific
import io.gitlab.arturbosch.smartsmells.smells.cycle.Cycle
import io.gitlab.arturbosch.smartsmells.smells.cycle.Dependency
import spock.lang.Specification

/**
 * @author Artur Bosch
 */
class PatchCycleTest extends Specification {

	def file1 = new File(getClass().getResource("/patch/CycleBase.java").path)
	def cycle = new Cycle(
			new Dependency("CycleTwo", "CycleTwo cycleTwo;", SourcePath.of(file1.toPath()), SourceRange.of(4, 4, 9, 27)),
			new Dependency("CycleOne", "CycleOne cycleOne;", SourcePath.of(file1.toPath()), SourceRange.of(8, 8, 9, 27))
	)

	def "patch renamed cycle one"() {
		given:
		def file2 = new File(getClass().getResource("/patch/CycleOneMovedAndRenamed.java").path)
		when:
		def smell = patch(file1, file2, cycle)
		then:
		smell.secondSignature() == "CycleOneRenamed cycleOne;"
	}

	private static CycleSpecific patch(File file1, File file2, CycleSpecific cycle) {
		def patch = new ASTDiffTool().createPatchFor(new MappingTest.SFile(file1), new MappingTest.SFile(file2))
		def patched = patch.patchSmell(new JavaCodeSmell(Smell.CYCLE, cycle))
		return patched.smell as Cycle
	}

}
