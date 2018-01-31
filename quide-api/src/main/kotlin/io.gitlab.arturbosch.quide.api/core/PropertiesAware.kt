package io.gitlab.arturbosch.quide.api.core

/**
 * @author Artur Bosch
 */
interface PropertiesAware {
	fun property(key: String): String?
	fun propertyOrDefault(key: String, defaultValue: String): String
}

const val PATHS_FILTERS_GLOBAL = "input.paths.filters.global"
const val PLATFORM_ADDITIONAL_PROPERTIES = "platform.additional.properties"
