package io.gitlab.arturbosch.quide.java.mapping

import com.github.javaparser.ast.Node
import io.gitlab.arturbosch.quide.java.JavaCodeSmell
import io.gitlab.arturbosch.quide.vcs.Patch

/**
 * @author Artur Bosch
 */
class ASTPatch(originalElements: List<Node>, revisedElements: List<Node>) : Patch<JavaCodeSmell> {

	override fun patchSmell(smell: JavaCodeSmell): JavaCodeSmell {
		return when {
			smell.ofClass() -> smell.patchClassLevel()
			smell.ofMethod() -> smell.patchMethodLevel()
			else -> smell
		}
	}

	private fun JavaCodeSmell.patchMethodLevel(): JavaCodeSmell {
		return this
	}

	private fun JavaCodeSmell.patchClassLevel(): JavaCodeSmell {
		return this
	}

}