package io.gitlab.arturbosch.quide.crawler.pipe

import io.gitlab.arturbosch.quide.crawler.Console
import io.gitlab.arturbosch.smartsmells.api.MetricFacade
import io.gitlab.arturbosch.smartsmells.metrics.Metric
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
object Analyze {

	private val facade = MetricFacade()

	fun start(path: Path) {
		val result = facade.run(path, listOf(".*/src/test/.*"))
		val averages = MetricFacade.averageAndDeviation(result)
		val resultString = averages.joinToString("\n") { "${it.type}=${it.asNumber()}" }
		Console.write("Results for ${path.fileName}:\n\t" + resultString)
		Write.toFile(path, resultString)
		Write.toDsl(path, averages)
	}

	private fun Metric.asNumber(): Number = if (isDouble) asDouble() else value
}
