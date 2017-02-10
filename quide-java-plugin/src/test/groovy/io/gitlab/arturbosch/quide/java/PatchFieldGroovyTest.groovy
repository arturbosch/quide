package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.jpal.ast.source.SourcePath
import io.gitlab.arturbosch.jpal.ast.source.SourceRange
import io.gitlab.arturbosch.quide.java.mapping.ASTDiffTool
import io.gitlab.arturbosch.smartsmells.config.Smell
import io.gitlab.arturbosch.smartsmells.smells.ElementTarget
import io.gitlab.arturbosch.smartsmells.smells.FieldSpecific
import io.gitlab.arturbosch.smartsmells.smells.deadcode.DeadCode
import spock.lang.Specification

/**
 * @author Artur Bosch
 */
class PatchFieldGroovyTest extends Specification {

	def file1 = new File(getClass().getResource("/patch/Base.java").path)
	def deadCode = new DeadCode("value", "private int value;", SourcePath.of(file1.toPath(), file1.toPath()),
			SourceRange.of(3, 3, 5, 23), ElementTarget.FIELD)

	def "patch unnamed field"() {
		given:
		def file2 = new File(getClass().getResource("/patch/FieldRenamed.java").path)
		when:
		def smell = patch(file1, file2, deadCode)
		then:
		smell.signature() == "private int myInt;"
	}

	def "patch moved field"() {
		given:
		def file2 = new File(getClass().getResource("/patch/FieldMoved.java").path)
		when:
		def smell = patch(file1, file2, deadCode)
		then:
		smell.signature() == "private int value;"
	}

	def "patch unnamed and moved field"() {
		given:
		def file2 = new File(getClass().getResource("/patch/FieldRenamedAndMoved.java").path)
		when:
		def smell = patch(file1, file2, deadCode)
		then:
		smell.signature() == "private int myInt;"
	}

	private static FieldSpecific patch(File file1, File file2, FieldSpecific deadCode) {
		def patch = new ASTDiffTool().createPatchFor(new MappingTest.SFile(file1), new MappingTest.SFile(file2))
		def patched = patch.patchSmell(new JavaCodeSmell(Smell.DEAD_CODE, deadCode))
		return patched.smell as DeadCode
	}

}
