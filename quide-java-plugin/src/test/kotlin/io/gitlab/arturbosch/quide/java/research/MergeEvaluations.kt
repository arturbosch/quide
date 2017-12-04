package io.gitlab.arturbosch.quide.java.research

import java.io.File

/**
 * @author Artur Bosch
 */

fun main(args: Array<String>) {
	val userHome = System.getProperty("user.home") ?: throw IllegalStateException()
	val root = File("$userHome/master/reports")

	val evaluationFiles = root.walkTopDown()
			.filter { it.isFile }
			.filter { it.toString().endsWith("evaluation.txt") }
			.toList()

	val result = evaluationFiles.map { it to it.readText() }
			.map { (file, text) ->
				try {
					ContainerEvaluationData.from(text)
				} catch (ex: NumberFormatException) {
					println(file)
					println(text)
					throw ex
				}
			}
			.reduce { acc, other -> acc + other }

	printFindings(result)
}

private fun printFindings(result: ContainerEvaluationData) {
	val distribution = result.typeData.map { it.type to it.survivalData.alive }.toMap()
	println(distribution.keys.map { typeLU[it] })
	println(distribution.values)
	println()

	println("Survival Data sorted by deadAliveRatio")
	result.typeData.sortedBy { it.survivalData.deadAliveRatio }
			.reversed()
			.forEach { println(it.asLatexTableRow()) }
	println()

	println("Lifespan")
	result.typeData.sortedBy { it.lifespanData.mean }
			.reversed()
			.forEach { println(typeLU[it.type] + " & " + it.lifespanData.asLatexTable()) }
	println()

	println("Changes")
	result.typeData.sortedBy { it.changesData.mean }
			.reversed()
			.forEach { println(typeLU[it.type] + " & " + it.changesData.asLatexTable()) }
	println()

	println("Relocations")
	result.typeData.sortedBy { it.relocationsData.mean }
			.reversed()
			.forEach { println(typeLU[it.type] + " & " + it.relocationsData.asLatexTable()) }
	println()

	println("Revivals")
	result.typeData.sortedBy { it.revivalsData.mean }
			.reversed()
			.forEach { println(typeLU[it.type] + " & " + it.revivalsData.asLatexTable()) }
	println()

}
