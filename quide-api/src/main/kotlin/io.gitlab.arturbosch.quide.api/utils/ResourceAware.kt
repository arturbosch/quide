package io.gitlab.arturbosch.quide.api.utils

import java.io.InputStream
import java.net.URL

/**
 * @author Artur Bosch
 */
interface ResourceAware {
	fun resource(name: String): URL = javaClass.getResource(name.asResourceName())
			?: throw IllegalArgumentException("There is no resource with name $name!")

	fun resourceStream(name: String): InputStream = javaClass.getResourceAsStream(name.asResourceName())
			?: throw IllegalArgumentException("There is no resource with name $name!")
}

private fun String.asResourceName() = if (startsWith("/")) this else "/$this"
