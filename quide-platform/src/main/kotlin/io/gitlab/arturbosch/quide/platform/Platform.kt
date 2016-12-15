package io.gitlab.arturbosch.quide.platform

/**
 * @author Artur Bosch
 */
class Platform(private val pluginLoader: PluginLoader) : ControlFlow {

	private val _plugins = lazy {
		pluginLoader.load()
	}

	override fun plugins(): List<Plugin> {
		return _plugins.value
	}

}