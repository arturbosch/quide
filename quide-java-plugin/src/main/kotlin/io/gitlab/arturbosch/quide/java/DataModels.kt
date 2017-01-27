package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.quide.model.BaseCodeSmell
import io.gitlab.arturbosch.quide.model.SmellContainer
import io.gitlab.arturbosch.smartsmells.api.SmellResult
import io.gitlab.arturbosch.smartsmells.common.DetectionResult

/**
 * @author Artur Bosch
 */
class JavaCodeSmell(private val smell: DetectionResult) : BaseCodeSmell() {
	init {
		sourcePath = smell.pathAsString
	}
	fun compact() = smell.asCompactString()
	override fun toString(): String = smell.toString()
}

class JavaSmellContainer(smells: SmellResult? = null) : SmellContainer<JavaCodeSmell> {
	val codeSmells: MutableList<JavaCodeSmell> = smells?.smellSets?.values
			?.flatMap { it }
			?.filter { it != null }
			?.map(::JavaCodeSmell)
			?.toMutableList() ?: mutableListOf()

	override fun all(): MutableList<JavaCodeSmell> {
		return codeSmells
	}

	override fun findBySourcePath(path: String): MutableList<JavaCodeSmell> {
		return codeSmells.filter { it.isLocatedAt(path) }.toMutableList()
	}
}