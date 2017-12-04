package io.gitlab.arturbosch.quide.java.research

import java.io.File

/**
 * @author Artur Bosch
 */
fun main(args: Array<String>) {
	val userHome = System.getProperty("user.home") ?: throw IllegalStateException()
	val root = File("$userHome/master/reports/save")

	val allFiles = root.walkTopDown()
			.filter { it.isFile }
			.filter { it.toString().endsWith("evaluation.txt") || it.toString().endsWith(".xml") }
			.toList()
	val (xmlFiles, evalFiles) = allFiles.partition { it.toString().endsWith(".xml") }

	xmlFiles.map {
		val path = it.toString()
		FileVersion(path.substringBefore("."), path.substringAfter(".").substringBefore(".").toInt())
	}.map { fv -> fv.version to evalFiles.find { it.toString().startsWith(fv.project) } }
			.forEach { doStuff(it) }
}

fun doStuff(it: Pair<Int, File?>) {
	println("File: ${it.second} with ${it.first}")
	if (it.second != null) {
		val (version, file) = it.first to it.second!!
		val result = file.readText().split(NL).joinToString(NL) { value ->
			val parts = value.split(TAB).toMutableList()
			if (parts.size != 1) {
				parts.add(3, version.toString())
			}
			parts.joinToString(TAB)
		}
		println(result + NL + NL)
		file.writeText(result)
	}
}

data class FileVersion(val project: String, val version: Int)
