package io.gitlab.arturbosch.quide.api.fs

/**
 * @author Artur Bosch
 */
interface InputDir : InputPath {
	override fun isDir(): Boolean = true
}
