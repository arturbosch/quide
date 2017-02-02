package io.gitlab.arturbosch.quide.java.mapping

import com.github.javaparser.ast.Node
import difflib.DiffUtils
import difflib.Patch
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