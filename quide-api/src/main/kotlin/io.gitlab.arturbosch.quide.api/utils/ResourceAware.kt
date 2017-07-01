package io.gitlab.arturbosch.quide.api.utils

/**
 * @author Artur Bosch
 */
interface ResourceAware {
	fun resource(name: String) = javaClass.getResource(name.asResourceName())
	fun resourceStream(name: String) = javaClass.getResourceAsStream(name.asResourceName())
}

private fun String.asResourceName() = if (startsWith("/")) this else "/$this"
