package io.gitlab.arturbosch.quide.java.mapping

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.expr.Expression
import com.github.javaparser.ast.stmt.Statement
import difflib.Delta
import io.gitlab.arturbosch.quide.java.JavaCodeSmell
import io.gitlab.arturbosch.quide.vcs.Patch
import io.gitlab.arturbosch.smartsmells.smells.ClassSpecific
import io.gitlab.arturbosch.smartsmells.smells.CycleSpecific
import io.gitlab.arturbosch.smartsmells.smells.ElementTarget
import io.gitlab.arturbosch.smartsmells.smells.FieldSpecific
import io.gitlab.arturbosch.smartsmells.smells.LocalSpecific
import io.gitlab.arturbosch.smartsmells.smells.MethodSpecific
import io.gitlab.arturbosch.smartsmells.smells.cycle.Cycle
import io.gitlab.arturbosch.smartsmells.util.Strings

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
					.filterNotNull()
					.map { it to Strings.distance(it.toSignature(), smell.signature()) }
					.minBy { it.second }
					?.let {
						val node = it.first
						when (node) {
							is Expression -> {
								println("TREFFER!")
								val copy = smell.copy(node)
								return this.updateInternal(copy)
							}
							is Statement -> {
								println("TREFFER!")
								val copy = smell.copy(node)
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
			if (smell is Cycle) {
				smell.source.searchForField()?.let {
					println("TREFFER!")
					val copy = smell.copy(it)
					return this.updateInternal(copy)
				}
				smell.target.searchForField()?.let {
					println("TREFFER!")
					val copy = smell.copyOnSecond(it)
					return this.updateInternal(copy)
				}
			}
//			chunks.asSequence().map { it.nodeByFieldSignature(smell.signature()) }
//					.find { it != null }
//					?.let {
//						println("TREFFER!")
//						val copy = smell.copy(it)
//						return this.updateInternal(copy)
//					} ?: chunks.asSequence().map { it.nodeByFieldSignature(smell.secondSignature()) }
//					.find { it != null }
//					?.let {
//						println("TREFFER!")
//						val copy = smell.copyOnSecond(it)
//						return this.updateInternal(copy)
//					}
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
			smell.searchForField()?.let { this.updateInternal(smell.copy(it)) }
		}
		return this
	}

	private fun FieldSpecific.searchForField(): FieldDeclaration? {
		val directMappedMatches = chunks.map { it.nodeByFieldSignature(signature()) }
				.filterNotNull()
		// we need to consider insertions as a text diff is not able to provide exact information
		// about moved+renamed code. Text diff will tell us about a change AND an addition
		val matchingInserts = chunks.filter { it.type == Delta.TYPE.INSERT }
				.map { it.revisedNodes.filter { it is FieldDeclaration } }
				.flatMap { it }
		return matchingInserts.plus(directMappedMatches)
				.filterIsInstance(FieldDeclaration::class.java)
				.map { it to Strings.distance(it.toSignature(), signature()) }
				.minBy { it.second }
				?.first
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
