package io.gitlab.arturbosch.quide.java.research

import org.assertj.core.api.Assertions
import org.junit.Test

/**
 * @author Artur Bosch
 */
class DataObjectsTest {

	@Test
	fun convertSurvivalData() {
		val actual = SurvivalData(100, 45, 55)
		val content = actual.toString()
		val expected = SurvivalData.from(content)

		Assertions.assertThat(expected).isEqualTo(actual)
	}

	@Test
	fun survivalSummaryBehavesLikeNormalSurvivalData() {
		val actual = SurvivalSummaryData(SurvivalData(100, 45, 55))
		val content = actual.toString()
		val expected = SurvivalSummaryData.from(content)

		Assertions.assertThat(expected).isEqualTo(actual)
	}

	@Test
	fun convertStatisticData() {
		val actual = LifespanData(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
		val content = actual.toString()
		val expected = LifespanData.from(content)

		Assertions.assertThat(expected).isEqualTo(actual)
		Assertions.assertThat(expected.max).isEqualTo(actual.max)
		Assertions.assertThat(expected.min).isEqualTo(actual.min)
		Assertions.assertThat(expected.mean).isEqualTo(actual.mean)
		Assertions.assertThat(expected.deviation).isEqualTo(actual.deviation)
		Assertions.assertThat(expected.sum).isEqualTo(actual.sum)
	}

	private fun smellTypeDataFor(name: String) = SmellTypeData(name, 10,
			SurvivalData(10, 4, 6),
			LifespanData(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)),
			ChangesData(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)),
			RelocationsData(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)),
			RevivalsData(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)))

	@Test
	fun convertSmellTypeData() {
		val actual = smellTypeDataFor("LongMethod")
		val content = actual.toString()
		val expected = SmellTypeData.from(content)

		Assertions.assertThat(expected).isEqualTo(actual)
	}

	@Test
	fun convertContainerEvaluationData() {
		val survivalData = SurvivalSummaryData(SurvivalData(100, 45, 55))
		val lmData = smellTypeDataFor("LongMethod")
		val lplData = smellTypeDataFor("LongParameterList")

		val actual = ContainerEvaluationData(survivalData, listOf(lmData, lplData))
		val content = actual.toString()
		val expected = ContainerEvaluationData.from(content)

		Assertions.assertThat(expected).isEqualTo(actual)
	}
}