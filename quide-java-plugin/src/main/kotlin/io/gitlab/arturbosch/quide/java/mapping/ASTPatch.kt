package io.gitlab.arturbosch.quide.java.mapping

import com.github.javaparser.ast.CompilationUnit
import io.gitlab.arturbosch.quide.java.JavaCodeSmell
import io.gitlab.arturbosch.quide.vcs.Patch
import io.gitlab.arturbosch.smartsmells.smells.MethodSpecific

/**
 * @author Artur Bosch
 */
class ASTPatch(val chunks: List<ASTDiffTool.AstChunk>, val unit: CompilationUnit) : Patch<JavaCodeSmell> {

	override fun patchSmell(smell: JavaCodeSmell): JavaCodeSmell {
		return when {
			smell.ofClass() -> smell.patchClassLevel()
			smell.ofMethod() -> smell.patchMethodLevel()
			else -> smell
		}
	}

	private fun JavaCodeSmell.patchMethodLevel(): JavaCodeSmell {
		val smell = this.smell
		if (smell is MethodSpecific) {
			chunks.find { it.nodeByMethodSignature(smell.signature()) != null }
					?.nodeByMethodSignature(smell.signature())?.let {
				println("TREFFER!")
				val copy = smell.copy(it)
				return this.updateInternal(copy)
			} ?: unit.types.flatMap { it.methods }
					.find { it.declarationAsString == smell.signature() }?.let {
				println("TREFFER!")
				val copy = smell.copy(it)
				return this.updateInternal(copy)
			}
		}
		return this
	}

	private fun JavaCodeSmell.patchClassLevel(): JavaCodeSmell {
		return this
	}

}