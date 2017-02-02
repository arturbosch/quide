package io.gitlab.arturbosch.quide.java.mapping

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.visitor.TreeVisitor
import difflib.Chunk
import difflib.Delta
import io.gitlab.arturbosch.jpal.ast.ClassHelper
import io.gitlab.arturbosch.jpal.internal.Printer
import io.gitlab.arturbosch.quide.vcs.DiffTool
import io.gitlab.arturbosch.quide.vcs.SourceFile

/**
 * @author Artur Bosch
 */
class ASTDiffTool : DiffTool<ASTPatch> {

	override fun createPatchFor(oldFile: SourceFile, newFile: SourceFile): ASTPatch {
		val oldUnit = JavaParser.parse(oldFile.content())
		val newUnit = JavaParser.parse(newFile.content())
		val diffPatch = textDiff(oldFile.content(), newFile.content())
		diffPatch.deltas.forEach(::println)
		return ASTDiffer(oldUnit, newUnit, diffPatch).patch().apply {
			chunks.forEach(::println)
		}
	}

	inner class ASTDiffer(private val oldUnit: CompilationUnit,
						  private val newUnit: CompilationUnit,
						  diffPatch: difflib.Patch<String>) {


		val chunks = diffPatch.deltas
				.map { AstChunk(it.type, it.original.astElements(oldUnit), it.revised.astElements(newUnit)) }

		private fun <T> Chunk<T>.astElements(unit: CompilationUnit): List<Node> {
			val filter = ElementsInRangeFilter(this)
			filter.visitBreadthFirst(unit)
			return filter.posToElement
		}

		fun patch(): ASTPatch {
			return ASTPatch(chunks, newUnit)
		}

	}

	data class AstChunk(val type: Delta.TYPE, val originalNodes: List<Node>, val revisedNodes: List<Node>) {

		private val cache = hashMapOf<String, Node>()

		fun nodeByMethodSignature(signature: String): MethodDeclaration? {
			return nodeBySignature(signature) { it.declarationAsString }
		}

		fun nodeByClassSignature(signature: String): ClassOrInterfaceDeclaration? {
			return nodeBySignature(signature) { ClassHelper.createFullSignature(it) }
		}

		inline private fun <reified T : Node> nodeBySignature(signature: String,
															  crossinline signatureFunction: (T) -> String): T? {
			return cache[signature] as T? ?: {
				val before = originalNodes.find { it is T } as T?
				val after = revisedNodes.find { it is T } as T?
				if (before != null && after != null && signatureFunction(before) == signature) {
					cache.put(signature, after)
					after
				} else null
			}.invoke()
		}

	}

	private class ElementsInRangeFilter(chunk: Chunk<*>) : TreeVisitor() {

		val start = chunk.position + 1
		val end = start + chunk.lines.size
		val text = chunk.lines.joinToString("\n").trim()

		val posToElement: MutableList<Node> = mutableListOf()

		override fun process(node: Node) {
			if (!node.range.isPresent) return // we need to compare positions
			if (node is CompilationUnit) return // Filter CU's if class change is searched

			// Best and fastest way if lines are same
			if (node.begin.get().line == start && node.end.get().line == end) {
				posToElement.add(node)
				return
			}

			if (isWithinMethod(node)) { // method signature comparison for performance
				posToElement.add(node)
				return
			}

			if (node.begin.get().line >= start && node.toString(Printer.NO_COMMENTS).contains(text)) {
				posToElement.add(node)
			}

		}

		private fun isWithinMethod(node: Node) = (node.begin.get().line >= start && node is MethodDeclaration
				&& text.contains(node.declarationAsString))

	}

}

