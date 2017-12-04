@file:Suppress("NOTHING_TO_INLINE")

package io.gitlab.arturbosch.quide.java.research

import io.gitlab.arturbosch.quide.java.core.JavaCodeSmell
import io.gitlab.arturbosch.quide.java.core.JavaSmellContainer
import java.util.ArrayList

/**
 * @author Artur Bosch
 */

fun evaluateToCSV(container: JavaSmellContainer, version: Int): String {
	val evaluationData = evaluateToDataObjects(container, version)
	return evaluationData.toString()
}

internal fun evaluateToDataObjects(container: JavaSmellContainer, version: Int): ContainerEvaluationData {
	val codeSmells = container.all().groupBy { it.type }
	val summaryData = SurvivalSummaryData(SurvivalData(
			container.all().size, container.alive().size, container.dead().size))
	val result = ArrayList<SmellTypeData>()
	for ((type, smells) in codeSmells) {
		val typeData = SmellTypeData(type.name, smells.size,
				SurvivalData(smells.size, smells.filter { it.isAlive }.size, smells.filterNot { it.isAlive }.size),
				LifespanData(smells.map { it.lifespanInDays() }, version),
				ChangesData(smells.map { it.weight() }),
				RelocationsData(smells.map { it.relocations().size }),
				RevivalsData(smells.map { it.revivedInVersions().size }))
		result.add(typeData)
	}
	return ContainerEvaluationData(summaryData, result)
}

private fun JavaCodeSmell.lifespanInDays() = (endVersion().versionNumber() - startVersion().versionNumber()) + 1
