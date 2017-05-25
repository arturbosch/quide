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
	}

	private val EXPECTED = """
--- START Survival Ratio ---
All, Alive, Dead, %Alive, %Dead, Ratio
12.0,12.0,0.0,1.0,0.0,0.0
--- END Survival Ratio ---
--- START Metrics per SmellType ---
COMMENT,COMPLEX_CONDITION,COMPLEX_METHOD,CYCLE,DATA_CLASS,DEAD_CODE,FEATURE_ENVY,GOD_CLASS,JAVADOC,LARGE_CLASS,LONG_METHOD,LONG_PARAM,MESSAGE_CHAIN,MIDDLE_MAN,NESTED_BLOCK_DEPTH,SHOTGUN_SURGERY,STATE_CHECKING
--- Type Survival ---
0.0.Infin,0.0,0.0,0.0,0.0.Infin,0.0.Infin,0.0.Infin,0.0,0.0,0.0,0.0.Infin,0.0.Infin,0.0,0.0,0.0,0.0,0.0
--- Type Occurrence ---
2,0,0,0,1,6,1,0,0,0,1,1,0,0,0,0,0
--- Type Lifespan ---
4.0,0.0,0.0,0.0,4.0,4.0,4.0,0.0,0.0,0.0,4.0,4.0,0.0,0.0,0.0,0.0,0.0
--- Type Changes ---
0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
--- Type Relocations ---
0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
--- Type Revivals ---
0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0
--- END Metrics per SmellType ---
""".trimIndent()
}