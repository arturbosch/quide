package io.gitlab.arturbosch.quide.api.core

import java.io.File

/**
 * @author Artur Bosch
 */
interface QuideDir : PropertiesAware {
	val home: File
	val pluginsDir: File
	val configurationsDir: File
	fun resolve(subPath: String): File
}
