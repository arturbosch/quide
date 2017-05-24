package io.gitlab.arturbosch.quide.java.research

import io.gitlab.arturbosch.quide.java.core.JavaSmellContainer
import io.gitlab.arturbosch.quide.java.safeContainer
import io.gitlab.arturbosch.quide.platform.ControlFlow
import io.gitlab.arturbosch.quide.platform.Processor
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.smartsmells.config.Smell

@Suppress("NOTHING_TO_INLINE")
/**
 * @author Artur Bosch
 */
class EvaluateContainerProcessor : Processor {

	override fun <U : UserData> execute(data: U) {
		val container = data.safeContainer().orElseThrow {
			InvalidContainerError("No container could be retrieved for evaluation!")
		}

		val result = with(StringBuilder()) {
			appendTotalSurvivalRatio(container)
			appendSmellTypes()
			appendSurvivalPerType(container)
			toString()
		}

		println(result)
	}

	private val smellTypes = Smell.values().filter { it != Smell.UNKNOWN && it != Smell.CLASS_INFO }

	private inline fun StringBuilder.comment(content: String) = append("--- $content ---\n")
	private inline fun StringBuilder.content(content: String) = append("$content\n")

	private inline fun StringBuilder.appendSmellTypes() {
		comment("START Metrics per SmellType")
		content(smellTypes.joinToString(","))
		comment("END Metrics per SmellType")
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
		content("$allSize,$alive,$dead,$alivePercentage,$deadPercentage,$ratio")
		comment("END Survival Ratio")
	}

	private fun StringBuilder.appendSurvivalPerType(container: JavaSmellContainer) {
		comment("START Type Survival")
		val codeSmells = container.all().groupBy { it.type }
		smellTypes.map { type -> codeSmells[type] }
		comment("END Type Survival")
	}

	override fun injectionPoint(): ControlFlow.InjectionPoint {
		return ControlFlow.InjectionPoint.AfterAnalysis
	}

	override fun priority(): Int {
		return 1000
	}
}

class InvalidContainerError(msg: String) : RuntimeException(msg)
