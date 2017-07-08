package io.gitlab.arturbosch.quide.java.research

import io.gitlab.arturbosch.quide.model.Printable

/**
 * @author Artur Bosch
 */
data class ContainerEvaluationData(val summaryData: SurvivalSummaryData,
								   val typeData: List<SmellTypeData>) : Printable {
	override fun toString(): String =
			with(StringBuilder()) {
				append(summaryData.toString())
				append(NL)
				typeData.forEach {
					append(it.toString())
					append(NL)
				}
				toString()
			}

	override fun asPrintable(): String {
		return "Survival Summary: " + summaryData.asPrintable() + NL + NL +
				typeData.joinToString(NL) { it.asPrintable() }
	}

	companion object {
		fun from(csv: String): ContainerEvaluationData {
			val lines = csv.split(NL).filterNot { it.isNullOrBlank() }
			require(lines.isNotEmpty())
			val summaryData = SurvivalSummaryData.from(lines[0])
			val typeData = if (lines.size > 1) {
				lines.subList(1, lines.size)
						.map { SmellTypeData.from(it) }
			} else emptyList<SmellTypeData>()
			return ContainerEvaluationData(summaryData, typeData)
		}
	}
}

const val TAB = "\t"
const val COMMA = ","
const val NL = "\n"

data class SurvivalSummaryData(val survivalData: SurvivalData) : Printable {
	override fun toString(): String = survivalData.toString()
	override fun asPrintable(): String = survivalData.asPrintable()

	companion object {
		fun from(csv: String) = SurvivalSummaryData(SurvivalData.from(csv))
	}
}

data class SmellTypeData(val type: String,
						 val occurrence: Int,
						 val survivalData: SurvivalData,
						 val lifespanData: LifespanData,
						 val changesData: ChangesData,
						 val relocationsData: RelocationsData,
						 val revivalsData: RevivalsData) : Printable {
	override fun toString(): String = type + TAB + occurrence + TAB + survivalData + TAB +
			lifespanData + TAB + changesData + TAB + relocationsData + TAB + revivalsData

	override fun asPrintable(): String = "$type - #$occurrence" + NL + "Survival: ${survivalData.asPrintable()}" + NL +
			"Lifespan: ${lifespanData.asPrintable()}" + NL + "Changes: ${changesData.asPrintable()}" + NL +
			"Relocations: ${relocationsData.asPrintable()}" + NL + "Revivals: ${revivalsData.asPrintable()}" + NL

	companion object {
		fun from(csv: String): SmellTypeData {
			val type = csv.substringBefore(TAB)
			val csvWithoutType = csv.substringAfter(TAB)
			val occurrence = csvWithoutType.substringBefore(TAB).toInt()
			val csvWithoutOccurrence = csvWithoutType.substringAfter(TAB)
			val survival = SurvivalData.from(csvWithoutOccurrence.substringBefore(TAB))
			val csvWithoutSurvival = csvWithoutOccurrence.substringAfter(TAB)
			val lifespan = LifespanData.from(csvWithoutSurvival.substringBefore(TAB))
			val csvWithoutLifespan = csvWithoutSurvival.substringAfter(TAB)
			val changes = ChangesData.from(csvWithoutLifespan.substringBefore(TAB))
			val csvWithoutChanges = csvWithoutLifespan.substringAfter(TAB)
			val relocations = RelocationsData.from(csvWithoutChanges.substringBefore(TAB))
			val csvWithoutRelocations = csvWithoutChanges.substringAfter(TAB)
			val revivals = RevivalsData.from(csvWithoutRelocations)
			return SmellTypeData(type, occurrence, survival, lifespan, changes, relocations, revivals)
		}
	}
}

class LifespanData(values: List<Int>) : StatisticsData(values) {
	companion object {
		fun from(csv: String) = LifespanData(StatisticsData.from(csv))
	}
}

class ChangesData(values: List<Int>) : StatisticsData(values) {
	companion object {
		fun from(csv: String) = ChangesData(StatisticsData.from(csv))
	}
}

class RelocationsData(values: List<Int>) : StatisticsData(values) {
	companion object {
		fun from(csv: String) = RelocationsData(StatisticsData.from(csv))
	}
}

class RevivalsData(values: List<Int>) : StatisticsData(values) {
	companion object {
		fun from(csv: String) = RevivalsData(StatisticsData.from(csv))
	}
}

abstract class StatisticsData(val values: List<Int>) : Printable {
	private val summary = values.stream().mapToInt { it }.summaryStatistics()
	val max get() = summary.max
	val min get() = summary.min
	val mean get() = summary.average
	val sum get() = summary.sum
	val deviation: Double get() = Math.sqrt(values.map { Math.pow(it.toDouble() - mean, 2.0) }.sum())

	override fun toString(): String = values.joinToString(COMMA)

	override fun asPrintable(): String = "sum=$sum, max=$max, min=$min, mean=$mean, deviation=$deviation"

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other?.javaClass != javaClass) return false

		other as StatisticsData

		if (values != other.values) return false

		return true
	}

	override fun hashCode(): Int {
		var result = values.hashCode()
		result = 31 * result + (summary?.hashCode() ?: 0)
		return result
	}

	companion object {
		fun from(csv: String) = csv.split(COMMA).map { it.toInt() }
	}

}

data class SurvivalData(val all: Int, val alive: Int, val dead: Int) : Printable {
	val aliveRatio: Double get() = alive.toDouble() / all
	val deadRatio get() = dead.toDouble() / all
	val deadAliveRatio get() = dead.toDouble() / alive

	override fun toString(): String = "$all,$alive,$dead,$aliveRatio,$deadRatio,$deadAliveRatio"

	override fun asPrintable(): String = "all=$all, alive=$alive, dead=$dead, " +
			"%alive=$aliveRatio, %dead=$deadRatio, deadForOneAlive=$deadAliveRatio"

	companion object {
		fun from(csv: String): SurvivalData {
			fun i(s: String) = s.toInt()
			val a = csv.split(COMMA)
			require(a.size == 6) { "Survival data expects six ints." }
			return SurvivalData(i(a[0]), i(a[1]), i(a[2]))
		}
	}

}