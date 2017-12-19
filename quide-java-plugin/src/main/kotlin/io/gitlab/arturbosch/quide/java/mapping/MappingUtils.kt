package io.gitlab.arturbosch.quide.java.mapping

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.stmt.ExpressionStmt
import com.github.javaparser.ast.stmt.Statement
import difflib.DiffUtils
import difflib.Patch
import io.gitlab.arturbosch.jpal.ast.ClassHelper
import io.gitlab.arturbosch.jpal.internal.Printer
import io.gitlab.arturbosch.smartsmells.smells.DetectionResult
import io.gitlab.arturbosch.smartsmells.smells.ElementTarget
import java.util.ArrayList

/**
 * @author Artur Bosch
 */
fun textDiff(file1: String, file2: String): Patch<String> {
	return DiffUtils.diff(file1.split("\n"), file2.split("\n"))
}

fun <N : Node> Node.nodesByType(clazz: Class<N>): List<N> {
	val nodes = ArrayList<N>()
	for (child in childNodes) {
		if (clazz.isInstance(child)) {
			nodes.add(clazz.cast(child))
		}
		nodes.addAll(child.nodesByType(clazz))
	}
	return nodes
}

fun Node.toMappableString(): String = Printer.toString(this)
fun Node.toSignature(): String = this.toMappableString()
fun ClassOrInterfaceDeclaration.toSignature(): String = ClassHelper.createFullSignature(this)

fun ASTChunk.containsSmell(smell: DetectionResult): Boolean {
	val candidates = originalNodes.filter { it.containsSmell(smell) }
	val result = when (smell.elementTarget()) {
		ElementTarget.LOCAL -> candidates.filter { it is ExpressionStmt || it is Statement }
		ElementTarget.FIELD -> candidates.filter { it is FieldDeclaration }
		ElementTarget.METHOD -> candidates.filter { it is MethodDeclaration }
		ElementTarget.CLASS -> candidates.filter { it is ClassOrInterfaceDeclaration }
		ElementTarget.PARAMETER -> candidates.filter { it is Parameter }
		else -> emptyList()
	}
	return result.isNotEmpty()
}
