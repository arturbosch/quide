package io.gitlab.arturbosch.quide.platform

/**
 * @author Artur Bosch
 */
object Platform : ControlFlow {

	private val _plugins = lazy {
		PluginLoader(PluginDetector()).load()
	}

	override fun plugins(): List<Plugin> {
		return _plugins.value
	}

}