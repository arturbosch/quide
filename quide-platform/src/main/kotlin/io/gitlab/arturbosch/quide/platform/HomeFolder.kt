package io.gitlab.arturbosch.quide.platform

/**
 * @author Artur Bosch
 */
object HomeFolder : DefaultQuideDirectory() {

	fun addPropertiesFromString(props: String) {
		loadPropertiesFromString(props)
	}

	fun addPropertyPairs(props: String) {
		props.splitToSequence(',')
				.map(String::trim)
				.map { it.split('=') }
				.filter { it.size == 2 }
				.forEach { properties.put(it[0], it[1]) }
	}
}

fun String.asProperty(): String? = HomeFolder.getProperty(this)