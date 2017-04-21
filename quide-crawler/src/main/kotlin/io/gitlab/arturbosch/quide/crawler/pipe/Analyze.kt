package io.gitlab.arturbosch.quide.crawler.pipe

import io.gitlab.arturbosch.quide.crawler.Console
import io.gitlab.arturbosch.smartsmells.api.MetricFacade
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
object Analyze {

	private val facade = MetricFacade.builder()
			.withFilters(listOf(".*/src/test/.*"))
			.fullStackFacade()

	fun start(path: Path) {
		val result = facade.run(path)
		val averages = MetricFacade.average(result)
		val resultString = averages.joinToString("\n\t")
		Console.write("Results for ${path.fileName}:\n\t" + resultString)
		Write.toFile(path, resultString)
	}
}