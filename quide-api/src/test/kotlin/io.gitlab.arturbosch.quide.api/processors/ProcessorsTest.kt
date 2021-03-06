package io.gitlab.arturbosch.quide.api.processors

import io.gitlab.arturbosch.quide.TestContext
import io.gitlab.arturbosch.quide.TestPlugin
import io.gitlab.arturbosch.quide.api.AnalysisContext
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it

/**
 * @author Artur Bosch
 */
class ProcessorsTest : Spek({

	val key = "test"
	val processor = object : EvolutionaryAnalysisProcessor {
		override fun onExecution(context: AnalysisContext) {
			context.put(key, "TEST")
		}
	}

	it("should not execute processor as test context is no evolutionary analysis") {
		val context = TestContext(TestPlugin)
		processor.execute(context)
		assertThat(context.get<String>(key)).isNull()
	}
})
