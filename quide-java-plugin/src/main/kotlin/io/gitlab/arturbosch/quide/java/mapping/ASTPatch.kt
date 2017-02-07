package io.gitlab.arturbosch.quide.java.mapping

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.expr.Expression
import com.github.javaparser.ast.stmt.Statement
import io.gitlab.arturbosch.jpal.ast.ClassHelper
import io.gitlab.arturbosch.jpal.internal.Printer
import io.gitlab.arturbosch.quide.java.JavaCodeSmell
import io.gitlab.arturbosch.quide.vcs.Patch
import io.gitlab.arturbosch.smartsmells.smells.ClassSpecific
import io.gitlab.arturbosch.smartsmells.smells.CycleSpecific
import io.gitlab.arturbosch.smartsmells.smells.ElementTarget
import io.gitlab.arturbosch.smartsmells.smells.FieldSpecific
import io.gitlab.arturbosch.smartsmells.smells.LocalSpecific
import io.gitlab.arturbosch.smartsmells.smells.MethodSpecific

/**
 * @author Artur Bosch
 */
class ASTPatch(val chunks: List<ASTChunk>, unit: CompilationUnit) : Patch<JavaCodeSmell> {

	private val methods = unit.types.flatMap { it.methods }
	private val fields = unit.types.flatMap { it.fields }
	private val types = unit.nodesByType(ClassOrInterfaceDeclaration::class.java)

	override fun patchSmell(smell: JavaCodeSmell): JavaCodeSmell {
		return when (smell.smell.elementTarget()) {
			ElementTarget.CLASS -> smell.patchClassLevel()
			ElementTarget.METHOD -> smell.patchMethodLevel()
			ElementTarget.PARAMETER -> smell.patchMethodLevel()
			ElementTarget.TWO_CLASSES -> smell.patchCycleLevel()
			ElementTarget.FIELD -> smell.patchFieldLevel()
			ElementTarget.LOCAL -> smell.patchLocalLevel()
			else -> smell
		}
	}

	private fun JavaCodeSmell.patchLocalLevel(): JavaCodeSmell {
		val smell = this.smell
		if (smell is LocalSpecific) {
			chunks.asSequence().map { it.anyNodeBySignature(smell.signature()) }
					.find { it != null }
					?.let {
						when (it) {
							is Expression -> {
								println("TREFFER!")
								val copy = smell.copy(it)
								return this.updateInternal(copy)
							}
							is Statement -> {
								println("TREFFER!")
								val copy = smell.copy(it)
								return this.updateInternal(copy)
							}
							else -> return@let
						}
					}
		}
		return this
	}

	private fun JavaCodeSmell.patchCycleLevel(): JavaCodeSmell {
		val smell = this.smell
		if (smell is CycleSpecific) {
			fields.find { it.toSignature() == smell.signature() }?.let {
				println("TREFFER!")
				val copy = smell.copy(it)
				return this.updateInternal(copy)
			} ?: fields.find { it.toSignature() == smell.secondSignature() }?.let {
				println("TREFFER!")
				val copy = smell.copyOnSecond(it)
				return this.updateInternal(copy)
			}
			chunks.asSequence().map { it.nodeByFieldSignature(smell.signature()) }
					.find { it != null }
					?.let {
						println("TREFFER!")
						val copy = smell.copy(it)
						return this.updateInternal(copy)
					} ?: chunks.asSequence().map { it.nodeByFieldSignature(smell.secondSignature()) }
					.find { it != null }
					?.let {
						println("TREFFER!")
						val copy = smell.copyOnSecond(it)
						return this.updateInternal(copy)
					}
		}
		return this
	}

	private fun JavaCodeSmell.patchFieldLevel(): JavaCodeSmell {
		val smell = this.smell
		if (smell is FieldSpecific) {
			fields.find { it.toSignature() == smell.signature() }?.let {
				println("TREFFER!")
				val copy = smell.copy(it)
				return this.updateInternal(copy)
			}
			chunks.asSequence().map { it.nodeByFieldSignature(smell.signature()) }
					.find { it != null }
					?.let {
						println("TREFFER!")
						val copy = smell.copy(it)
						return this.updateInternal(copy)
					}
		}
		return this
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
		val smell = this.smell
		if (smell is ClassSpecific) {
			types.find { it.toSignature() == smell.signature() }
					?.let {
						println("TREFFER!")
						val copy = smell.copy(it)
						return this.updateInternal(copy)
					}
			chunks.asSequence().map { it.nodeByClassSignature(smell.signature()) }
					.find { it != null }
					?.let {
						println("TREFFER!")
						val copy = smell.copy(it)
						return this.updateInternal(copy)
					}
		}
		return this
	}

}

fun Node.toSignature(): String = toString(Printer.NO_COMMENTS)
fun ClassOrInterfaceDeclaration.toSignature(): String = ClassHelper.createFullSignature(this)
