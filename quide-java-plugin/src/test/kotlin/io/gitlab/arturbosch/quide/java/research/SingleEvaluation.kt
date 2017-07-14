package io.gitlab.arturbosch.quide.java.research

import java.io.File

/**
 * @author Artur Bosch
 */
fun main(args: Array<String>) {
	val name = args[0]
	val userHome = System.getProperty("user.home") ?: throw IllegalStateException()
	val root = File("$userHome/reports").resolve(name)
	val evaluationData = ContainerEvaluationData.from(root.readText())
	println(evaluationData.asPrintable())
}