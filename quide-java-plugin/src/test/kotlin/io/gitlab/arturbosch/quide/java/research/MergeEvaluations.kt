package io.gitlab.arturbosch.quide.java.research

import java.io.File

/**
 * @author Artur Bosch
 */

fun main(args: Array<String>) {
	val userHome = System.getProperty("user.home") ?: throw IllegalStateException()
	val file = File("$userHome/reports/quide.evaluation.txt")
	val content = file.readText()
	val container = ContainerEvaluationData.from(content)
	println(container.asPrintable())
}