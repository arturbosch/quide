package io.gitlab.arturbosch.quide.api.core

/**
 * @author Artur Bosch
 */
interface PropertiesAware {
	fun property(key: String): String?
	fun propertyOrDefault(key: String, defaultValue: String): String
}