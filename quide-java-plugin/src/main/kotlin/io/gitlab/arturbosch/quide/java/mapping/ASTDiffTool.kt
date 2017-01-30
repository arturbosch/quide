package io.gitlab.arturbosch.quide.java.mapping

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.visitor.TreeVisitor
import difflib.Chunk
import difflib.Delta
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
		diffPatch.deltas.forEach { println(it) }
		return ASTDiffer(oldUnit, newUnit, diffPatch).patch().apply {
			chunks.forEach { println(it) }
		}
	}

	inner class ASTDiffer(private val oldUnit: CompilationUnit,
						  private val newUnit: CompilationUnit,
						  diffPatch: difflib.Patch<String>) {


		val chunks = diffPatch.deltas
				.map { AstChunk(it.type, it.original.astElements(oldUnit), it.revised.astElements(newUnit)) }

		private fun <T> Chunk<T>.astElements(unit: CompilationUnit): List<Node> {
			val start = position + 1
			val end = start + lines.size
			val filter = ElementsInRangeFilter(start, end)
			filter.visitBreadthFirst(unit)
			return filter.posToElement
		}

		fun patch(): ASTPatch {
			return ASTPatch(chunks)
		}

	}

	data class AstChunk(val type: Delta.TYPE, val originalNodes: List<Node>, val revisedNodes: List<Node>) {

		private val cache = hashMapOf<String, Node>()

		fun nodeByMethodSignature(signature: String): MethodDeclaration? {
			return cache[signature] as MethodDeclaration? ?: {
				val before = originalNodes.find { it is MethodDeclaration } as MethodDeclaration?
				val after = revisedNodes.find { it is MethodDeclaration } as MethodDeclaration?
				if (before != null && after != null && before.declarationAsString == signature) {
					cache.put(signature, after)
					after
				} else null
			}.invoke()
		}

	}

	private class ElementsInRangeFilter(val start: Int, val end: Int) : TreeVisitor() {

		val posToElement: MutableList<Node> = mutableListOf()

		override fun process(node: Node) {
			if (node.begin.get().line >= start && node.end.get().line <= end) {
				posToElement.add(node)
			}
		}

	}

}

