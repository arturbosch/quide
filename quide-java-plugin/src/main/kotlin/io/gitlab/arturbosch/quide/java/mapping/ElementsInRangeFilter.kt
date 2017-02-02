package io.gitlab.arturbosch.quide.java.mapping

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.visitor.TreeVisitor
import difflib.Chunk
import io.gitlab.arturbosch.jpal.internal.Printer

/**
 * @author Artur Bosch
 */
internal class ElementsInRangeFilter(chunk: Chunk<*>) : TreeVisitor() {

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