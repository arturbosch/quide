package io.gitlab.arturbosch.quide.java.core

import io.gitlab.arturbosch.quide.model.BaseCodeSmell
import io.gitlab.arturbosch.smartsmells.config.Smell
import io.gitlab.arturbosch.smartsmells.smells.DetectionResult

/**
 * @author Artur Bosch
 */
class JavaCodeSmell(val type: Smell, var smell: DetectionResult) : BaseCodeSmell() {

	init {
		sourcePath = smell.pathAsString
	}

	internal fun overridePathTestOnly(path: String) {
		sourcePath = path
	}

	var compareString: String = smell.asComparableString()

	override fun toString(): String = smell.toString() + "\n\t" + super.toString()

	override fun asPrintable(): String = smell.toString()

	fun updateInternal(updated: DetectionResult): JavaCodeSmell {
		return this.apply {
			smell = updated
			addWeight(1)
			compareString = smell.asComparableString()
		}
	}

	fun compare(second: JavaCodeSmell): Boolean {
		return compareString == second.compareString
	}

	fun compareWithoutPath(second: JavaCodeSmell): Boolean {
		return signatureWithoutPath() == second.signatureWithoutPath()
	}

	fun JavaCodeSmell.signatureWithoutPath(): String {
		val wholeSplit = compareString.split("$")
		return wholeSplit[0] + "$" + wholeSplit.subList(2, wholeSplit.size).joinToString("$")
	}

}

