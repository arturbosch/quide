package io.gitlab.arturbosch.quide.java.mapping

import com.github.javaparser.ast.CompilationUnit
import io.gitlab.arturbosch.quide.java.JavaCodeSmell
import io.gitlab.arturbosch.quide.vcs.Patch
import io.gitlab.arturbosch.smartsmells.smells.MethodSpecific

/**
 * @author Artur Bosch
 */
class ASTPatch(val chunks: List<ASTDiffTool.AstChunk>, val unit: CompilationUnit) : Patch<JavaCodeSmell> {

	private val methods = unit.types.flatMap { it.methods }
	private val fields = unit.types.flatMap { it.fields }
	private val types = unit.types

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
			methods.find { it.declarationAsString == smell.signature() }?.let {
				println("TREFFER!")
				val copy = smell.copy(it)
				return this.updateInternal(copy)
			}
			chunks.asSequence().map { it.nodeByMethodSignature(smell.signature()) }
					.find { it != null }
					?.let {
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