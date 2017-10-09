package io.gitlab.arturbosch.quide.java.research

import io.gitlab.arturbosch.quide.java.TestVersion
import io.gitlab.arturbosch.quide.java.lint
import org.junit.Test
import kotlin.test.assertEquals

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

		val content = evaluateToCSV(container)
		assertEquals(content.trimIndent(), EXPECTED)

		val actualContainer = evaluateToDataObjects(container)
		val expectedContainer = ContainerEvaluationData.from(content)
		assertEquals(actualContainer, expectedContainer)
	}

	private val EXPECTED = """
12,12,0,1.0,0.0,0.0
COMMENT	2	2,2,0,1.0,0.0,0.0	4,4	0,0	0,0	0,0
DEAD_CODE	6	6,6,0,1.0,0.0,0.0	4,4,4,4,4,4	0,0,0,0,0,0	0,0,0,0,0,0	0,0,0,0,0,0
LONG_METHOD	1	1,1,0,1.0,0.0,0.0	4	0	0	0
LONG_PARAM	1	1,1,0,1.0,0.0,0.0	4	0	0	0
DATA_CLASS	1	1,1,0,1.0,0.0,0.0	4	0	0	0
FEATURE_ENVY	1	1,1,0,1.0,0.0,0.0	4	0	0	0
""".trimIndent()
}