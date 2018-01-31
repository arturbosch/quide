package io.gitlab.arturbosch.quide.platform

import io.gitlab.arturbosch.quide.core.QuideDirectory
import java.io.File

/**
 * @author Artur Bosch
 */
const val USER_HOME = "user.home"
const val QUIDE_DIR_NAME = ".quide"

object HomeFolder : QuideDirectory(
		File(System.getProperty(USER_HOME), QUIDE_DIR_NAME),
		mutableMapOf()) {

	fun addPropertiesFromString(props: String) {
		loadPropertiesFromString(props)
	}

	fun addPropertyPairs(props: String) {
		props.splitToSequence(',')
				.map(String::trim)
				.map { it.split('=') }
				.filter { it.size == 2 }
				.forEach { properties[it[0]] = it[1] }
	}
}

fun String.asProperty(): String? = HomeFolder.property(this)
