package io.gitlab.arturbosch.quide.java.mapping

import com.github.javaparser.JavaParser
import com.github.javaparser.Position
import com.github.javaparser.Range
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.visitor.TreeVisitor
import difflib.Delta
import io.gitlab.arturbosch.quide.vcs.DiffTool
import io.gitlab.arturbosch.quide.vcs.SourceFile
import java.util.ArrayList
import java.util.HashMap

/**
 * @author Artur Bosch
 */
class ASTDiffTool : DiffTool<ASTPatch> {

	override fun createPatchFor(oldFile: SourceFile, newFile: SourceFile): ASTPatch {
		val oldUnit = JavaParser.parse(oldFile.content())
		val newUnit = JavaParser.parse(newFile.content())
		val diffPatch = textDiff(oldFile.content(), newFile.content())
		diffPatch.deltas.forEach { println(it) }
		return ASTDiffer(oldUnit, newUnit, diffPatch).patch()
	}

	inner class ASTDiffer(oldUnit: CompilationUnit,
						  newUnit: CompilationUnit,
						  diffPatch: difflib.Patch<String>) {

		private val originalElements: List<Node>
		private val revisedElements: List<Node>

		init {
			val newRanges = diffPatch.deltas
					.filterNot { it.type == Delta.TYPE.DELETE }
					.map { it.revised }
					.map { Range(Position(it.position, 1), Position(it.position + it.lines.size, 1)) }

			val oldRanges = diffPatch.deltas
					.filterNot { it.type == Delta.TYPE.INSERT }
					.map { it.original }
					.map { Range(Position(it.position, 1), Position(it.position + it.lines.size, 1)) }

			val visitor = ElementsInRangeFilter(newRanges)
			visitor.visitBreadthFirst(newUnit)
			revisedElements = visitor.posToElement.values.toList()

			println("New: ")
			visitor.posToElement.forEach { println(it.key); println(it.value.range); println(it.value) }
			println("\n")

			val visitor2 = ElementsInRangeFilter(oldRanges)
			visitor2.visitBreadthFirst(oldUnit)
			originalElements = visitor2.posToElement.values.toList()

			println("Old: ")
			visitor2.posToElement.forEach { println(it.key); println(it.value.range); println(it.value) }

		}


		fun patch(): ASTPatch {
			return ASTPatch(originalElements, revisedElements)
		}

	}

	private class ElementsInRangeFilter(deltas: List<Range>) : TreeVisitor() {

		private val _deltas = ArrayList(deltas)
		val posToElement: HashMap<Range, Node> = hashMapOf()

		override fun process(node: Node) {
			_deltas.find {
				node.range.get().isAfter(it.begin)
			}?.let {
				if (posToElement[it] == null) {
					posToElement.put(it, node)
					_deltas.remove(it)
				}
			}

		}

	}

}