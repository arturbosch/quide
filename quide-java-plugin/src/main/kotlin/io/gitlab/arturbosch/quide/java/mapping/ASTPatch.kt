package io.gitlab.arturbosch.quide.java.mapping

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.nodeTypes.NodeWithCondition
import com.github.javaparser.ast.stmt.Statement
import difflib.Delta
import io.gitlab.arturbosch.jpal.internal.Printer
import io.gitlab.arturbosch.kutils.peek
import io.gitlab.arturbosch.quide.java.core.JavaCodeSmell
import io.gitlab.arturbosch.quide.vcs.Patch
import io.gitlab.arturbosch.smartsmells.smells.ClassSpecific
import io.gitlab.arturbosch.smartsmells.smells.CycleSpecific
import io.gitlab.arturbosch.smartsmells.smells.ElementTarget
import io.gitlab.arturbosch.smartsmells.smells.FieldSpecific
import io.gitlab.arturbosch.smartsmells.smells.LocalSpecific
import io.gitlab.arturbosch.smartsmells.smells.MethodSpecific
import io.gitlab.arturbosch.smartsmells.smells.complexcondition.ComplexCondition
import io.gitlab.arturbosch.smartsmells.smells.cycle.Cycle
import io.gitlab.arturbosch.smartsmells.smells.deadcode.DeadCode
import io.gitlab.arturbosch.smartsmells.smells.messagechain.MessageChain
import io.gitlab.arturbosch.smartsmells.smells.statechecking.StateChecking
import io.gitlab.arturbosch.smartsmells.util.Strings
import org.apache.logging.log4j.LogManager

/**
 * @author Artur Bosch
 */
class ASTPatch(val chunks: List<ASTChunk>, val unit: CompilationUnit) : Patch<JavaCodeSmell> {

	private val logger = LogManager.getLogger()

	private val methods = unit.types.flatMap { it.methods }
	private val fields = unit.types.flatMap { it.fields }
	private val types = unit.nodesByType(ClassOrInterfaceDeclaration::class.java)

	override fun patchSmell(smell: JavaCodeSmell): JavaCodeSmell {
		return try {
			when (smell.smell.elementTarget()) {
				ElementTarget.CLASS -> smell.patchClassLevel()
				ElementTarget.METHOD -> smell.patchMethodLevel()
				ElementTarget.PARAMETER -> smell.patchMethodLevel()
				ElementTarget.TWO_CLASSES -> smell.patchCycleLevel()
				ElementTarget.FIELD -> smell.patchFieldLevel()
				ElementTarget.LOCAL -> smell.patchLocalLevel()
				else -> smell
			}
		} catch (e: RuntimeException) {
			logger.warn("Recover from failing mapping for $smell", e)
			logger.warn(unit.toString())
			smell.markDirty()
			return smell
		}
	}

	private fun JavaCodeSmell.patchLocalLevel(): JavaCodeSmell {
		val smell = this.smell
		if (smell is LocalSpecific) {
			chunks.asSequence()
					.map { it.anyNodeBySignature(smell.signature()) }
					.filterNotNull()
					.map { it to Strings.distance(it.toSignature(), smell.signature()) }
					.minBy { it.second }
					?.let { return this.updateInternal(smell.copy(it.first)) }
			when (smell) {
				is ComplexCondition -> {
					val oldSignature = smell.cases.joinToString(",")
					val stmts = chunks.asSequence()
							.filter { it.containsSmell(smell) }
							.flatMap { it.revisedNodes.asSequence() }
							.filter { it is NodeWithCondition<*> }
							.map { it as NodeWithCondition<*> }
							.toList()
					findWithSimilarCondition(oldSignature, stmts)?.let {
						val updated = smell.copy(it as Statement)
						return this.updateInternal(updated)
					}
				}
				is DeadCode, is MessageChain, is StateChecking -> {
					chunks.filter { it.containsSmell(smell) }
							.flatMap { it.revisedNodes }
							.peek { println(it) }
							.map { it to Strings.distance(smell.signature(), it.toString(Printer.NO_COMMENTS)) }
							.minBy { it.second }
							?.let { return this.updateInternal(smell.copy(it.first)) }
				}
			}
		}
		return this
	}

	private fun JavaCodeSmell.patchCycleLevel(): JavaCodeSmell {
		val smell = this.smell
		if (smell is CycleSpecific) {
			fields.find { it.toSignature() == smell.signature() }?.let {
				val copy = smell.copy(it)
				return this.updateInternal(copy)
			} ?: fields.find { it.toSignature() == smell.secondSignature() }?.let {
				val copy = smell.copyOnSecond(it)
				return this.updateInternal(copy)
			}
			if (smell is Cycle) {
				smell.source.searchForField()?.let {
					val copy = smell.copy(it)
					return this.updateInternal(copy)
				}
				smell.target.searchForField()?.let {
					val copy = smell.copyOnSecond(it)
					return this.updateInternal(copy)
				}
			}
		}
		return this
	}

	private fun JavaCodeSmell.patchFieldLevel(): JavaCodeSmell {
		val smell = this.smell
		if (smell is FieldSpecific) {
			fields.find { it.toSignature() == smell.signature() }?.let {
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
				val copy = smell.copy(it)
				return this.updateInternal(copy)
			}
			chunks.asSequence().map { it.nodeByMethodSignature(smell.signature()) }
					.find { it != null }
					?.let {
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
						val copy = smell.copy(it)
						return this.updateInternal(copy)
					}
			chunks.asSequence().map { it.nodeByClassSignature(smell.signature()) }
					.find { it != null }
					?.let {
						val copy = smell.copy(it)
						return this.updateInternal(copy)
					}
		}
		return this
	}

}