package io.gitlab.arturbosch.quide.java.research

import io.gitlab.arturbosch.quide.model.Printable
import org.nield.kotlinstatistics.descriptiveStatistics
import java.text.DecimalFormat

/**
 * @author Artur Bosch
 */
data class ContainerEvaluationData(val summaryData: SurvivalSummaryData,
								   val typeData: List<SmellTypeData>) : Printable {


	operator fun plus(other: ContainerEvaluationData): ContainerEvaluationData {
		val survivalSummaryData = summaryData + other.summaryData

		val otherTypeData = other.typeData
		val newTypeData = typeData.map { (type, occurrence, survivalData, lifespanData, changesData, relocationsData, revivalsData) ->
			val otherData = otherTypeData.find { it.type == type }
			SmellTypeData(type, otherData?.occurrence?.plus(occurrence) ?: occurrence,
					otherData?.survivalData?.plus(survivalData) ?: survivalData,
					otherData?.lifespanData?.plus(lifespanData) ?: lifespanData,
					otherData?.changesData?.plus(changesData) ?: changesData,
					otherData?.relocationsData?.plus(relocationsData) ?: relocationsData,
					otherData?.revivalsData?.plus(revivalsData) ?: revivalsData)
		}
		return ContainerEvaluationData(survivalSummaryData, newTypeData)
	}

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
			val lines = csv.split(NL).filterNot { it.isBlank() }
			require(lines.isNotEmpty())
			val summaryData = SurvivalSummaryData.from(lines[0])
			val typeData = if (lines.size > 1) {
				lines.subList(1, lines.size)
						.map { SmellTypeData.from(it) }
			} else emptyList()
			return ContainerEvaluationData(summaryData, typeData)
		}
	}
}

const val TAB = "\t"
const val COMMA = ","
const val NL = "\n"

data class SurvivalSummaryData(private val survivalData: SurvivalData) : Printable {
	override fun toString(): String = survivalData.toString()
	override fun asPrintable(): String = survivalData.asPrintable()
	operator fun plus(other: SurvivalSummaryData): SurvivalSummaryData
			= SurvivalSummaryData(survivalData.plus(other.survivalData))

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

	fun asLatexTableRow(): String {
		val d = survivalData
		val (all, alive, dead) = survivalData
		return "${typeLU[type]} & $all & $alive & $dead & ${f.format(d.aliveRatio)} & ${f.format(d.deadRatio)} & " +
				"${f.format(d.deadAliveRatio)} \\\\\\hline"
	}

	companion object {
		fun from(csv: String): SmellTypeData {
			val type = csv.substringBefore(TAB)
			val csvWithoutType = csv.substringAfter(TAB)
			val occurrence = csvWithoutType.substringBefore(TAB).toInt()
			val csvWithoutOccurrence = csvWithoutType.substringAfter(TAB)
			val survival = SurvivalData.from(csvWithoutOccurrence.substringBefore(TAB))
			val csvWithoutSurvival = csvWithoutOccurrence.substringAfter(TAB)
			val revisionsNumber = csvWithoutSurvival.substringBefore(TAB).toInt()
			val csvWithoutRevisionsNumber = csvWithoutSurvival.substringAfter(TAB)
			val lifespan = LifespanData.from(revisionsNumber, csvWithoutRevisionsNumber.substringBefore(TAB))
			val csvWithoutLifespan = csvWithoutRevisionsNumber.substringAfter(TAB)
			val changes = ChangesData.from(csvWithoutLifespan.substringBefore(TAB))
			val csvWithoutChanges = csvWithoutLifespan.substringAfter(TAB)
			val relocations = RelocationsData.from(csvWithoutChanges.substringBefore(TAB))
			val csvWithoutRelocations = csvWithoutChanges.substringAfter(TAB)
			val revivals = RevivalsData.from(csvWithoutRelocations)
			return SmellTypeData(type, occurrence, survival, lifespan, changes, relocations, revivals)
		}
	}
}

class LifespanData(val version: Int = 100, val values: List<Double>) : Printable, LatexTable {

	constructor(values: List<Int>, version: Int = 100) : this(version, values.map { it.toDouble() / version * 100 })

	private val summary = values.filter { it > 0 }.toDoubleArray().descriptiveStatistics
	val max get() = summary.max
	val min get() = summary.min
	val mean get() = summary.mean
	val sum get() = summary.sum
	val deviation: Double get() = summary.standardDeviation
	val firstQuantil: Double get() = summary.percentile(25.0)
	val thirdQuantil: Double get() = summary.percentile(75.0)
	val median: Double get() = summary.percentile(50.0)
	val size: Long get() = summary.size

	operator fun plus(other: LifespanData) = LifespanData(values = other.values + values)

	override fun toString() = version.toString() + TAB + values.joinToString(COMMA)

	override fun asPrintable(): String =
			"size=$size sum=$sum, min=$min, 1st=$firstQuantil median=$median mean=${f.format(mean)}, " +
					"3rd=$thirdQuantil " +
					"max=$max, " +
					"deviation=${f.format(deviation)}"

	override fun asLatexTable(): String =
			"${f.format(min)} & " +
					"${f.format(firstQuantil)} & " +
					"${f.format(median)} & " +
					"${f.format(mean)} & " +
					"${f.format(thirdQuantil)} & " +
					"${f.format(max)} & " +
					"${f.format(deviation)} \\\\\\hline"

	companion object {
		fun from(revision: Int, csv: String) = LifespanData(revision, csv.split(COMMA).map { it.toDouble() })
	}
}

class ChangesData(values: List<Int>) : StatisticsData(values) {

	operator fun plus(other: ChangesData) = ChangesData(other.values + values)

	companion object {
		fun from(csv: String) = ChangesData(StatisticsData.from(csv))
	}
}

class RelocationsData(values: List<Int>) : StatisticsData(values) {

	operator fun plus(other: RelocationsData) = RelocationsData(other.values + values)

	companion object {
		fun from(csv: String) = RelocationsData(StatisticsData.from(csv))
	}
}

class RevivalsData(values: List<Int>) : StatisticsData(values) {

	operator fun plus(other: RevivalsData) = RevivalsData(other.values + values)

	companion object {
		fun from(csv: String) = RevivalsData(StatisticsData.from(csv))
	}
}

abstract class StatisticsData(val values: List<Int>) : Printable, LatexTable {
	private val summary = values.filter { it > 0 }.toIntArray().descriptiveStatistics
	open val max get() = summary.max
	open val min get() = summary.min
	open val mean get() = summary.mean
	open val sum get() = summary.sum
	open val deviation: Double get() = summary.standardDeviation
	open val firstQuantil: Double get() = summary.percentile(25.0)
	open val thirdQuantil: Double get() = summary.percentile(75.0)
	open val median: Double get() = summary.percentile(50.0)
	open val size: Long get() = summary.size

	override fun toString(): String = values.joinToString(COMMA)
	override fun asPrintable(): String =
			"size=$size sum=$sum, min=$min, 1st=$firstQuantil median=$median mean=${f.format(mean)}, " +
					"3rd=$thirdQuantil " +
					"max=$max, " +
					"deviation=${f.format(deviation)}"

	override fun asLatexTable(): String =
			"$size & " +
					"${sum.toLong()} & " +
					"$median & ${f.format(mean)} & " +
					"${f.format(thirdQuantil)} & " +
					"$max & " +
					"${f.format(deviation)} \\\\\\hline"

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other?.javaClass != javaClass) return false

		other as StatisticsData

		if (values != other.values) return false

		return true
	}

	override fun hashCode(): Int {
		var result = values.hashCode()
		result = 31 * result + summary.hashCode()
		return result
	}

	companion object {
		fun from(csv: String) = csv.split(COMMA).map { it.toInt() }

	}

}

private val f = DecimalFormat("#0.00")

data class SurvivalData(val all: Int,
						val alive: Int,
						val dead: Int) : Printable {

	val aliveRatio: Double get() = alive.toDouble() / all * 100
	val deadRatio get() = dead.toDouble() / all * 100
	val deadAliveRatio get() = dead.toDouble() / alive

	override fun toString(): String = "$all,$alive,$dead,$aliveRatio,$deadRatio,$deadAliveRatio"

	override fun asPrintable(): String = "all=$all, alive=$alive, dead=$dead, " +
			"%alive=${f.format(aliveRatio)}, %dead=${f.format(deadRatio)}, deadForOneAlive=$deadAliveRatio"

	operator fun plus(other: SurvivalData): SurvivalData = run {
		val (allO, aliveO, deadO) = other
		SurvivalData(all + allO, alive + aliveO, dead + deadO)
	}

	companion object {
		fun from(csv: String): SurvivalData {
			val a = csv.split(COMMA)
			require(a.size == 6) { "Survival data expects six ints." }
			return SurvivalData(a[0].toInt(), a[1].toInt(), a[2].toInt())
		}

	}

}

interface LatexTable {
	fun asLatexTable(): String
}

val typeLU = mapOf(
		"FEATURE_ENVY" to "FeatureEnvy",
		"COMPLEX_CONDITION" to "ComplexCondition",
		"LONG_PARAM" to "LongParameterList",
		"CYCLE" to "Cycle",
		"LONG_METHOD" to "LongMethod",
		"COMPLEX_METHOD" to "ComplexMethod",
		"DATA_CLASS" to "DataClass",
		"DEAD_CODE" to "DeadCode",
		"NESTED_BLOCK_DEPTH" to "NestedBlockDepth",
		"LARGE_CLASS" to "LargeClass",
		"SHOTGUN_SURGERY" to "ShotgunSurgery",
		"GOD_CLASS" to "GodClass",
		"CLASS_DATA_SHOULD_BE_PRIVATE" to "ClassDataShouldBePrivate",
		"BRAIN_METHOD" to "BrainMethod",
		"TRADITION_BREAKER" to "TraditionBreaker",
		"REFUSED_PARENT_BEQUEST" to "RefusedParentBequest"
)
