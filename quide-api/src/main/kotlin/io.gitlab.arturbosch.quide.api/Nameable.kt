package io.gitlab.arturbosch.quide.api

/**
 * @author Artur Bosch
 */
interface Nameable {

	val name get(): String = javaClass.simpleName
}