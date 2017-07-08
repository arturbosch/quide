@file:Suppress("NOTHING_TO_INLINE")

package io.gitlab.arturbosch.quide.java.research

import io.gitlab.arturbosch.quide.java.core.JavaCodeSmell
import io.gitlab.arturbosch.quide.java.core.JavaSmellContainer
import io.gitlab.arturbosch.smartsmells.config.Smell
import java.util.ArrayList

/**
 * @author Artur Bosch
 */

private val smellTypes = Smell.values().filter { it != Smell.UNKNOWN && it != Smell.CLASS_INFO }

fun evaluateToDataObjects(container: JavaSmellContainer): ContainerEvaluationData {
	val codeSmells = container.all().groupBy { it.type }
	val summaryData = SurvivalSummaryData(SurvivalData(
			container.all().size, container.alive().size, container.dead().size))
	val result = ArrayList<SmellTypeData>()
	for ((type, smells) in codeSmells) {
		val typeData = SmellTypeData(type.name, smells.size,
				SurvivalData(smells.size, smells.filter { it.isAlive }.size, smells.filterNot { it.isAlive }.size),
				LifespanData(smells.map { it.lifespanInDays() }),
				ChangesData(smells.map { it.weight() }),
				RelocationsData(smells.map { it.relocations().size }),
				RevivalsData(smells.map { it.revivedInVersions().size }))
		result.add(typeData)
	}
	return ContainerEvaluationData(summaryData, result)
}

fun evaluateToCSV(container: JavaSmellContainer): String {
	val codeSmells = container.all().groupBy { it.type }

	return with(StringBuilder()) {
		appendTotalSurvivalRatio(container)
		comment("START Metrics per SmellType")
		appendSmellTypes()
		appendSurvivalPerType(codeSmells)
		appendOccurrencePerType(codeSmells)
		appendLifespanPerType(codeSmells)
		appendChangesPerType(codeSmells)
		appendRelocationsPerType(codeSmells)
		appendRevivalsPerType(codeSmells)
		comment("END Metrics per SmellType")
		toString()
	}
}

private inline fun StringBuilder.comment(content: String) = append("--- $content ---\n")
private inline fun StringBuilder.content(content: String) = append("$content\n")

private inline fun StringBuilder.appendSmellTypes() {
	content(smellTypes.joinToString(","))
}

private inline fun StringBuilder.appendTotalSurvivalRatio(container: JavaSmellContainer) {
	comment("START Survival Ratio")
	val codeSmells = container.all()
	val allSize = codeSmells.size.toDouble()
	val alive = container.alive().size.toDouble()
	val dead = container.dead().size.toDouble()
	val alivePercentage = alive / allSize
	val deadPercentage = dead / allSize
	val ratio = alivePercentage / deadPercentage
	content("All, Alive, Dead, %Alive, %Dead, Ratio")
	val content = "$allSize,$alive,$dead,$alivePercentage,$deadPercentage,$ratio"
	content(content.replace("Infinity", "0.0"))
	comment("END Survival Ratio")
}

private inline fun StringBuilder.appendSurvivalPerType(codeSmells: Map<Smell, List<JavaCodeSmell>>) {
	comment("Type Survival")
	val result = smellTypes.asSequence()
			.map { type -> codeSmells[type] }
			.map { it ?: emptyList() }
			.map { calculateDeadPerAliveRatio(it) }
			.toRoundedString()
	content(result.replace("Infinity", "0.0"))
}

private inline fun calculateDeadPerAliveRatio(smells: List<JavaCodeSmell>): Double {
	if (smells.isEmpty()) return 0.0
	val allSize = smells.size.toDouble()
	val (alive, dead) = smells.partition { it.isAlive }
	val alivePercentage = alive.size / allSize
	val deadPercentage = dead.size / allSize
	return alivePercentage / deadPercentage
}

private fun StringBuilder.appendOccurrencePerType(codeSmells: Map<Smell, List<JavaCodeSmell>>) {
	comment("Type Occurrence")
	val occurrences = smellTypes.asSequence()
			.map { codeSmells[it]?.size ?: 0 }
			.joinToString(",")
	content(occurrences)
}

private fun StringBuilder.appendLifespanPerType(codeSmells: Map<Smell, List<JavaCodeSmell>>) {
	comment("Type Lifespan")
	val occurrences = smellTypes.asSequence()
			.map { codeSmells[it] }
			.map { it?.meanBy { it.lifespanInDays() } ?: 0.0 }
			.toRoundedString()
	content(occurrences)
}

private fun StringBuilder.appendChangesPerType(codeSmells: Map<Smell, List<JavaCodeSmell>>) {
	comment("Type Changes")
	val changes = smellTypes.asSequence()
			.map { codeSmells[it] }
			.map { it?.meanBy { it.weight() } ?: 0.0 }
			.toRoundedString()
	content(changes)
}

private fun StringBuilder.appendRelocationsPerType(codeSmells: Map<Smell, List<JavaCodeSmell>>) {
	comment("Type Relocations")
	val relocations = smellTypes.asSequence()
			.map { codeSmells[it] }
			.map { it?.meanBy { it.relocations().size } ?: 0.0 }
			.toRoundedString()
	content(relocations)
}

private fun StringBuilder.appendRevivalsPerType(codeSmells: Map<Smell, List<JavaCodeSmell>>) {
	comment("Type Revivals")
	val relocations = smellTypes.asSequence()
			.map { codeSmells[it] }
			.map { it?.meanBy { it.revivedInVersions().size } ?: 0.0 }
			.toRoundedString()
	content(relocations)
}

val ROUND_CONSTANT = 5

private fun Sequence<Double>.toRoundedString() = joinToString(",") {
	val content = it.toString()
	val after = content.substringAfter(".")
	val before = content.substringBefore(".")
	before + "." + if (after.length <= ROUND_CONSTANT) after else after.substring(0, ROUND_CONSTANT)
}

private fun List<JavaCodeSmell>.meanBy(by: (JavaCodeSmell) -> Int): Double = if (isEmpty()) 0.0
else map { by(it) }.sum().toDouble() / size.toDouble()

private fun JavaCodeSmell.lifespanInDays() = endVersion().versionNumber() - startVersion().versionNumber()