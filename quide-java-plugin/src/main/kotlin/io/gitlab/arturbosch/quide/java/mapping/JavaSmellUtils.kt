package io.gitlab.arturbosch.quide.java.mapping

import com.github.javaparser.Range
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.Expression
import com.github.javaparser.ast.stmt.Statement
import io.gitlab.arturbosch.smartsmells.smells.DetectionResult
import io.gitlab.arturbosch.smartsmells.smells.LocalSpecific

/**
 * @author Artur Bosch
 */
fun DetectionResult.toJPRange(): Range = with(positions) {
	Range.range(startLine, startColumn, endLine, endColumn)
}

fun Node.containsSmell(smell: DetectionResult): Boolean {
	if (!range.isPresent) return false
	val jpRange = smell.toJPRange()
	return range.get().begin.line <= jpRange.begin.line && range.get().end.line >= jpRange.end.line
}

fun LocalSpecific.copy(node: Node): LocalSpecific = when (node) {
	is Statement -> copy(node)
	is Expression -> copy(node)
	else -> this
}