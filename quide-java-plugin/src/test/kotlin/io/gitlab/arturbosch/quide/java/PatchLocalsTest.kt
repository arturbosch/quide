package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.jpal.ast.source.SourcePath
import io.gitlab.arturbosch.jpal.ast.source.SourceRange
import io.gitlab.arturbosch.kutils.resource
import io.gitlab.arturbosch.quide.java.core.JavaCodeSmell
import io.gitlab.arturbosch.quide.java.mapping.ASTDiffTool
import io.gitlab.arturbosch.smartsmells.config.Smell
import io.gitlab.arturbosch.smartsmells.smells.ElementTarget
import io.gitlab.arturbosch.smartsmells.smells.LocalSpecific
import io.gitlab.arturbosch.smartsmells.smells.complexcondition.ComplexCondition
import io.gitlab.arturbosch.smartsmells.smells.deadcode.DeadCode
import io.gitlab.arturbosch.smartsmells.smells.messagechain.MessageChain
import io.gitlab.arturbosch.smartsmells.smells.statechecking.StateChecking
import org.junit.Ignore
import org.junit.Test
import java.io.File

/**
 * @author Artur Bosch
 */
class PatchLocalsTest {

	private val baseFile = File(resource("/patch/LocalsBase.java"))
	private val patchFile = File(resource("/patch/LocalsAddition.java"))

	@Ignore("Unmappable due to Text Diff")
	@Test
	fun patchMethodChain() {
		val messageChain = MessageChain("builder().doStuff().thenStuff().moreStuff().moreStuff()", "builder",
				"moreStuff", 4, 3, SourcePath.of(baseFile.toPath(), baseFile.toPath()),
				SourceRange.of(30, 33, 9, 29), ElementTarget.LOCAL)
		val smell = patch(messageChain)
		assert(smell.signature() == "builder().moreStuff().moreStuff().moreStuff().moreStuff()")
	}

	@Test
	fun patchDeadCode() {
		val deadCode = DeadCode("neverUsed", "int neverUsed = 5", SourcePath.of(baseFile.toPath(), baseFile.toPath()),
				SourceRange.of(28, 28, 9, 26), ElementTarget.LOCAL)

		val smell = patch(deadCode)

		assert(smell.signature() == "int neverUsed = -1")
	}

	@Test
	fun patchComplexCondition() {
		val complexCondition = ComplexCondition("LocalsBase#public static void main(String[] args)",
				"NORMAL == 0 && EASY == -1 && HARD == 1 || VERY_HARD == 2 || 42 != 1",
				mutableSetOf("NORMAL == 0", "EASY == -1", "HARD == 1", "VERY_HARD == 2", "42 != 1"), 3,
				SourcePath.of(baseFile.toPath(), baseFile.toPath()),
				SourceRange.of(24, 24, 13, 80), ElementTarget.LOCAL)

		val smell = patch(complexCondition)

		assert(smell.signature() == "NORMAL == 0 && EASY == -1 && VERY_EASY == -2 " +
				"&& HARD == 1 || VERY_HARD == 2 || 42 != 1")
	}

	@Ignore("Unmappable due to Text Diff")
	@Test
	fun patchStateChecking() {
		val stateCheck = StateChecking("LocalsBase#public static void main(String[] args)",
				mutableListOf("value == NORMAL", "value == EASY", "value == HARD", "value == VERY_HARD"),
				StateChecking.getSUBTYPING(),
				SourcePath.of(baseFile.toPath(), baseFile.toPath()), SourceRange.of(11, 19, 9, 10), ElementTarget.LOCAL)

		val smell = patch(stateCheck)
		println(smell.signature())

		assert(smell.signature() == "LocalsBase#public static void main(String[] args)#" +
				"value == NORMAL, value == EASY, value == VERY_EASY, value == HARD, value == VERY_HARD")
	}

	private fun patch(smell: LocalSpecific): LocalSpecific {
		val patch = ASTDiffTool().createPatchFor(MappingTest.SFile(baseFile), MappingTest.SFile(patchFile))
		val patched = patch.patchSmell(JavaCodeSmell(Smell.STATE_CHECKING, smell))
		return patched.smell as LocalSpecific
	}
}