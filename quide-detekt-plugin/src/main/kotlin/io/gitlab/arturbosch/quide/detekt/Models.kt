package io.gitlab.arturbosch.quide.detekt

import io.gitlab.arturbosch.detekt.api.Finding
import io.gitlab.arturbosch.quide.model.BaseCodeSmell
import io.gitlab.arturbosch.quide.model.SmellContainer

/**
 * @author Artur Bosch
 */

class DetektCodeSmell(private val smell: Finding) : BaseCodeSmell() {
	init {
		sourcePath = smell.location.file
	}

	override fun toString(): String = smell.compact()
}

class DetektSmellContainer(private val smells: List<DetektCodeSmell>) : SmellContainer<DetektCodeSmell> {

	private val _smells = smells.toMutableSet()
	override fun all(): MutableSet<DetektCodeSmell> {
		return _smells
	}

	override fun findBySourcePath(path: String): MutableList<DetektCodeSmell> {
		return _smells.filter { it.isLocatedAt(path) }.toMutableList()
	}
}
