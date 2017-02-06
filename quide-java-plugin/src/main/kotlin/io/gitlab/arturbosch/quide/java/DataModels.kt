package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.quide.model.BaseCodeSmell
import io.gitlab.arturbosch.quide.model.SmellContainer
import io.gitlab.arturbosch.smartsmells.api.SmellResult
import io.gitlab.arturbosch.smartsmells.smells.DetectionResult
import io.gitlab.arturbosch.smartsmells.config.Smell
import io.gitlab.arturbosch.smartsmells.out.XMLWriter

/**
 * @author Artur Bosch
 */
class JavaCodeSmell(private val type: Smell, var smell: DetectionResult) : BaseCodeSmell() {

	init {
		sourcePath = smell.pathAsString
	}

	var asXmlContent: String = toXmlContent()

	private fun toXmlContent(): String {
		return XMLWriter.toXml(type, smell).let {
			it.substring(0, it.indexOf("path"))
		}
	}

	override fun toString(): String = smell.toString() + "\n\t" + super.toString()

	fun updateInternal(updated: DetectionResult): JavaCodeSmell {
		return this.apply {
			smell = updated
			asXmlContent = toXmlContent()
		}
	}

	fun ofEverywhere() = when (type) {
		Smell.DEAD_CODE, Smell.COMMENT -> true
		else -> false
	}

	fun ofMethodOrClass() = when (type) {
		Smell.JAVADOC -> true
		else -> false
	}

	fun ofMethod() = when (type) {
		Smell.LONG_METHOD, Smell.COMPLEX_METHOD, Smell.LONG_PARAM, Smell.FEATURE_ENVY,
		Smell.NESTED_BLOCK_DEPTH -> true
		else -> false
	}

	fun ofClass() = when (type) {
		Smell.DATA_CLASS, Smell.GOD_CLASS, Smell.MIDDLE_MAN, Smell.SHOTGUN_SURGERY, Smell.LARGE_CLASS,
		Smell.CLASS_INFO, Smell.CYCLE -> true
		else -> false
	}

	fun ofLocal() = when (type) {
		Smell.MESSAGE_CHAIN, Smell.STATE_CHECKING -> true
		else -> false
	}

}

class JavaSmellContainer(smells: SmellResult? = null) : SmellContainer<JavaCodeSmell> {
	val codeSmells: MutableList<JavaCodeSmell> = smells?.smellSets
			?.map { entrySet -> entrySet.value.map { JavaCodeSmell(entrySet.key, it) } }
			?.flatMap { it }
			?.toMutableList() ?: mutableListOf()

	override fun all(): MutableList<JavaCodeSmell> {
		return codeSmells
	}

	override fun findBySourcePath(path: String): MutableList<JavaCodeSmell> {
		return codeSmells.filter { it.isLocatedAt(path) }.toMutableList()
	}
}