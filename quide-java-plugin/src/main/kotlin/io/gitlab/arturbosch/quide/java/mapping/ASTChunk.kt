package io.gitlab.arturbosch.quide.java.mapping

import com.github.javaparser.ast.Node
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import difflib.Delta

/**
 * @author Artur Bosch
 */
data class ASTChunk(val type: Delta.TYPE, val originalNodes: List<Node>, val revisedNodes: List<Node>) {

	private val cache = hashMapOf<String, Node>()

	fun anyNodeBySignature(signature: String): Node? {
		return nodeBySignature(signature, Node::toSignature)
	}

	fun nodeByFieldSignature(signature: String): FieldDeclaration? {
		return nodeBySignature(signature) { it.toSignature() }
	}

	fun nodeByMethodSignature(signature: String): MethodDeclaration? {
		return nodeBySignature(signature) { it.declarationAsString }
	}

	fun nodeByClassSignature(signature: String): ClassOrInterfaceDeclaration? {
		return nodeBySignature(signature) { it.toSignature() }
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