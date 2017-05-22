package io.gitlab.arturbosch.quide.crawler.interop

import io.gitlab.arturbosch.smartsmells.config.dsl.DetectorConfigDsl
import io.gitlab.arturbosch.smartsmells.config.dsl.DetectorConfigDslRunner
import java.nio.file.Path
import java.util.Optional

/**
 * @author Artur Bosch
 */
object GroovyDsl {

	private val dsl = DetectorConfigDslRunner.execute(
			javaClass.getResourceAsStream("/default-config.groovy").bufferedReader().lineSequence().joinToString("\n"))

	fun withConfiguredMetrics(path: Path, block: DetectorConfigDsl.() -> Unit): String {
		with(dsl) {
			input = path
			output = Optional.empty()
			block(dsl)
		}
		return dsl.print(0)
	}

}