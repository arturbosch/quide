package io.gitlab.arturbosch.quide.java.mapping

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import difflib.DiffUtils
import difflib.Patch
import io.gitlab.arturbosch.jpal.ast.ClassHelper
import io.gitlab.arturbosch.jpal.internal.Printer
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

fun Node.toSignature(): String = toString(Printer.NO_COMMENTS)
fun ClassOrInterfaceDeclaration.toSignature(): String = ClassHelper.createFullSignature(this)
