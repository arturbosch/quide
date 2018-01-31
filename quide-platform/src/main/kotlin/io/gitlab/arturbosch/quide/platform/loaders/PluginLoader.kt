package io.gitlab.arturbosch.quide.platform.loaders

import io.gitlab.arturbosch.quide.api.Plugin

/**
 * @author Artur Bosch
 */
interface PluginLoader {
	fun load(): List<Plugin>
}
