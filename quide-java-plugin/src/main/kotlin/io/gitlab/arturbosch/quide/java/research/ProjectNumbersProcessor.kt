package io.gitlab.arturbosch.quide.java.research

import io.gitlab.arturbosch.kutils.readText
import io.gitlab.arturbosch.kutils.write
import io.gitlab.arturbosch.quide.java.loadFiltersFromProperties
import io.gitlab.arturbosch.quide.java.withOutputPath
import io.gitlab.arturbosch.quide.platform.ControlFlow
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.quide.platform.processors.ConditionalProcessor
import io.gitlab.arturbosch.smartsmells.metrics.internal.LinesOfCode
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.util.regex.Pattern
import java.util.stream.Collectors

/**
 * @author Artur Bosch
 */
class ProjectNumbersProcessor : ConditionalProcessor {

	private val logger = LoggerFactory.getLogger(javaClass.simpleName)

	override fun <U : UserData> isActive(data: U) = data.isEvolutionaryAnalysis

	override fun <U : UserData> doIfActive(data: U) {
		data.withOutputPath { output, version, container ->
			val project = data.projectPath().fileName.toString()
			val file = output.resolve("$project.stats.txt")
			val sum = calculateLinesOfCode(data)
			file.write("$project,${version.versionNumber()},$sum,${container.all().size}")
			logger.info("Container saved to $file")
		}
	}

	private fun calculateLinesOfCode(data: UserData): Int {
		val filters = loadFiltersFromProperties(data.quideDirectory()).map { Pattern.compile(it) }
		val javaFiles = Files.walk(data.projectPath())
				.filter { it.toString().endsWith(".java") }
				.filter {
					val filename = it.fileName.toString()
					filename != "package-info.java" && filename != "module-info.java"
				}
				.filter { path -> filters.none { it.matcher(path.toString()).matches() } }
				.collect(Collectors.toSet())

		var sum = 0
		for (javaFile in javaFiles) {
			val linesOfCode = LinesOfCode()
			linesOfCode.analyze(*javaFile.readText().split("\n").toTypedArray())
			sum += linesOfCode.source
		}
		return sum
	}

	override fun injectionPoint() = ControlFlow.InjectionPoint.AfterAnalysis

	override fun priority() = 1001
}
