package io.gitlab.arturbosch.quide.java.research

import io.gitlab.arturbosch.quide.java.TestVersion
import io.gitlab.arturbosch.quide.java.lint
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author Artur Bosch
 */
class EvaluateContainerToCSVTest {

	@Test
	fun containerToEvaluationCSV() {
		val container = "repo/version1/Version.java".lint()
		container.codeSmells.forEach {
			it.setStartVersion(TestVersion(1))
			it.setEndVersion(TestVersion(5))
		}

		val content = evaluateToCSV(container, 5)
		assertEquals(content.trimIndent(), expected)
	}

	private val expected = """
12,12,0,100.0,0.0,0.0
COMMENT	2	2,2,0,100.0,0.0,0.0	5	100.0,100.0	0,0	0,0	0,0
DEAD_CODE	6	6,6,0,100.0,0.0,0.0	5	100.0,100.0,100.0,100.0,100.0,100.0	0,0,0,0,0,0	0,0,0,0,0,0	0,0,0,0,0,0
LONG_METHOD	1	1,1,0,100.0,0.0,0.0	5	100.0	0	0	0
LONG_PARAM	1	1,1,0,100.0,0.0,0.0	5	100.0	0	0	0
DATA_CLASS	1	1,1,0,100.0,0.0,0.0	5	100.0	0	0	0
FEATURE_ENVY	1	1,1,0,100.0,0.0,0.0	5	100.0	0	0	0
""".trimIndent()
}
