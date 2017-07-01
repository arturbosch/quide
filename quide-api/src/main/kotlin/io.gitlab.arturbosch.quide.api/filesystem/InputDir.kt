package io.gitlab.arturbosch.quide.api.filesystem

/**
 * @author Artur Bosch
 */
interface InputDir : InputPath {
	override fun isDir(): Boolean = true
}