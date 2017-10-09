package io.gitlab.arturbosch.quide.java.research

import java.io.File

/**
 * @author Artur Bosch
 */

fun main(args: Array<String>) {
	val userHome = System.getProperty("user.home") ?: throw IllegalStateException()
	val root = File("$userHome/reports")

	val evaluationFiles = root.walkTopDown()
			.filter { it.isFile }
			.filter { it.toString().endsWith("evaluation.txt") }
			.toList()

	val reducedEvaluationContainer = evaluationFiles.map { it.readText() }
			.map { ContainerEvaluationData.from(it) }
			.reduce { acc, other -> acc + other }

	println(reducedEvaluationContainer.asPrintable())
}