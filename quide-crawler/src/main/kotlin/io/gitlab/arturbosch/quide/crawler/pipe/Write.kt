package io.gitlab.arturbosch.quide.crawler.pipe

import io.gitlab.arturbosch.kutils.Try
import io.gitlab.arturbosch.kutils.write
import io.gitlab.arturbosch.quide.crawler.Console
import io.gitlab.arturbosch.quide.crawler.interop.GroovyDsl
import io.gitlab.arturbosch.smartsmells.metrics.Metric
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
object Write {

	fun toFile(path: Path, result: String) {
		val name = path.fileName.toString()
		val resultFile = path.parent.resolve("$name.txt")
		resultFile.tryToWrite(result)
	}

	private fun Path.tryToWrite(content: String) {
		Try {
			this.write(content)
		} onSuccess {
			Console.write("Wrote ${this.fileName} to $it")
		} onError {
			Console.write("Error when writing results for ${this.fileName}: $it")
			throw IllegalStateException()
		}
	}

	fun toDsl(path: Path, metrics: List<Metric>) {
		val name = path.fileName.toString()
		val scriptFile = path.parent.resolve("$name.groovy")

		val script = GroovyDsl.withConfiguredMetrics(path) {
			values["longmethod"]?.put("threshold", "${metrics.extractValue("LongMethod")}")
			values["longparameterlist"]?.put("threshold", "${metrics.extractValue("LongParameterList")}")
			values["complexmethod"]?.put("threshold", "${metrics.extractValue("MCCabe")}")
			values["largeclass"]?.put("threshold", "${metrics.extractValue("SourceLinesOfCode")}")
		}
		scriptFile.tryToWrite(script)
	}

	private fun List<Metric>.extractValue(name: String): Double {
		val mean = find { it.type == "${name}Mean" }?.asDouble() ?: throw IllegalStateException()
		val deviation = find { it.type == "${name}Deviation" }?.asDouble() ?: throw IllegalStateException()
		return mean + deviation
	}
}