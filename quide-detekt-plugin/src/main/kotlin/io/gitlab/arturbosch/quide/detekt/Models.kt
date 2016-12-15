package io.gitlab.arturbosch.quide.detekt

import io.gitlab.arturbosch.detekt.api.Finding
import io.gitlab.arturbosch.quide.model.BaseCodeSmell
import io.gitlab.arturbosch.quide.model.SmellContainer

/**
 * @author Artur Bosch
 */

class DetektCodeSmell(private val smell: Finding) : BaseCodeSmell() {
	override fun toString(): String = smell.compact()
}

class DetektSmellContainer(private val smells: List<DetektCodeSmell>) : SmellContainer<DetektCodeSmell> {
	override fun all(): MutableList<DetektCodeSmell> {
		return smells.toMutableList()
	}

	override fun findBySourcePath(path: String): MutableList<DetektCodeSmell> {
		return smells.filter { it.isLocatedAt(path) }.toMutableList()
	}
}