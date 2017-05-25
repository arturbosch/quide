package io.gitlab.arturbosch.quide.java.research

import io.gitlab.arturbosch.quide.java.JavaPlugin
import io.gitlab.arturbosch.quide.java.TestVersion
import io.gitlab.arturbosch.quide.java.lint
import io.gitlab.arturbosch.quide.platform.ContainerAware
import org.junit.Test

/**
 * @author Artur Bosch
 */
class EvaluateContainerProcessorTest {

	@Test
	fun containerToEvaluationCSV() {
		val container = "repo/version1/Version.java".lint()
		container.codeSmells.forEach {
			it.setStartVersion(TestVersion(1))
			it.setEndVersion(TestVersion(5))
		}
		val processor = EvaluateContainerProcessor()
		val data = with(JavaPlugin()) {
			userData().put(ContainerAware.CURRENT_CONTAINER, container)
			userData()
		}

		processor.execute(data)
	}
}