package io.gitlab.arturbosch.quide.java.mapping

import com.github.javaparser.ast.nodeTypes.NodeWithCondition
import io.gitlab.arturbosch.smartsmells.smells.complexcondition.Conditions
import io.gitlab.arturbosch.smartsmells.util.Strings

/**
 * @author Artur Bosch
 */
fun findWithSimilarCondition(oldCasesSignature: String, stmt: List<NodeWithCondition<*>>) = stmt
		.map {
			val newCases = mutableSetOf<String>()
			Conditions.whileBinaryExpression(newCases, it.condition)
			it to newCases.joinToString(",")
		}.map { it.first to Strings.distance(oldCasesSignature, it.second) }
		.minBy { it.second }?.first
