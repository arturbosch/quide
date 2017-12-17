package io.gitlab.arturbosch.quide.java.core

import io.gitlab.arturbosch.quide.model.SmellContainer
import io.gitlab.arturbosch.smartsmells.api.SmellResult

/**
 * @author Artur Bosch
 */
class JavaSmellContainer(val smells: SmellResult) : SmellContainer<JavaCodeSmell> {

	val codeSmells: MutableSet<JavaCodeSmell> = smells.smellSets
			?.map { entrySet ->
				entrySet.value.filterNotNull().map { JavaCodeSmell(entrySet.key, it) }
			}
			?.flatMap { it }
			?.toMutableSet() ?: mutableSetOf()

	override fun all(): MutableSet<JavaCodeSmell> {
		return codeSmells
	}
}
