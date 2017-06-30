package io.gitlab.arturbosch.quide.api

/**
 * @author Artur Bosch
 */
interface Nameable {

	fun name(): String {
		return javaClass.simpleName
	}
}