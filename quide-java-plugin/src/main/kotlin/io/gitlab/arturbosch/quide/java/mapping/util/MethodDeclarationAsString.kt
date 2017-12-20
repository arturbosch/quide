package io.gitlab.arturbosch.quide.java.mapping.util

import com.github.javaparser.ast.AccessSpecifier
import com.github.javaparser.ast.Modifier
import com.github.javaparser.ast.Modifier.getAccessSpecifier
import com.github.javaparser.ast.body.CallableDeclaration
import com.github.javaparser.ast.body.ConstructorDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import io.gitlab.arturbosch.jpal.internal.Printer

/**
 * Adapted declarationAsString methods from javaparser to avoid performance penalty from moveCursor of
 * PrettyPrinter in standard toString method.
 *
 * @author Artur Bosch
 */
fun CallableDeclaration<*>.toDeclarationString(): String =
		(this as? MethodDeclaration)?.toDeclarationString() ?: (this as ConstructorDeclaration).toDeclarationString()

fun MethodDeclaration.toDeclarationString(): String {
	val sb = StringBuilder()
	val accessSpecifier = getAccessSpecifier(modifiers)
	sb.append(accessSpecifier.asString())
	sb.append(if (accessSpecifier == AccessSpecifier.DEFAULT) "" else " ")
	if (modifiers.contains(Modifier.STATIC)) {
		sb.append("static ")
	}
	if (modifiers.contains(Modifier.ABSTRACT)) {
		sb.append("abstract ")
	}
	if (modifiers.contains(Modifier.FINAL)) {
		sb.append("final ")
	}
	if (modifiers.contains(Modifier.NATIVE)) {
		sb.append("native ")
	}
	if (modifiers.contains(Modifier.SYNCHRONIZED)) {
		sb.append("synchronized ")
	}
	sb.append(Printer.toString(type))
	sb.append(" ")
	sb.append(name)
	sb.append("(")
	var firstParam = true
	for (param in parameters) {
		if (firstParam) {
			firstParam = false
		} else {
			sb.append(", ")
		}
		sb.append(Printer.toString(param))
	}
	sb.append(")")
	sb.append(appendThrowsIfRequested(true))
	return sb.toString()
}

internal fun CallableDeclaration<*>.appendThrowsIfRequested(includingThrows: Boolean): String {
	val sb = StringBuilder()
	if (includingThrows) {
		var firstThrow = true
		for (thr in thrownExceptions) {
			if (firstThrow) {
				firstThrow = false
				sb.append(" throws ")
			} else {
				sb.append(", ")
			}
			sb.append(Printer.toString(thr))
		}
	}
	return sb.toString()
}

fun ConstructorDeclaration.toDeclarationString(): String {
	val sb = StringBuilder()
	val accessSpecifier = Modifier.getAccessSpecifier(modifiers)
	sb.append(accessSpecifier.asString())
	sb.append(if (accessSpecifier == AccessSpecifier.DEFAULT) "" else " ")
	sb.append(name)
	sb.append("(")
	var firstParam = true
	for (param in parameters) {
		if (firstParam) {
			firstParam = false
		} else {
			sb.append(", ")
		}
		sb.append(Printer.toString(param))
	}
	sb.append(")")
	sb.append(appendThrowsIfRequested(true))
	return sb.toString()
}
