package io.gitlab.arturbosch.quide.platform

import java.net.URLClassLoader
import java.util.ServiceLoader

/**
 * @author Artur Bosch
 */
interface PluginLoader {
	fun load(): List<Plugin>
}

class BasePluginLoader(val pluginDetector: PluginDetector) : PluginLoader {

	private val logger by logFactory()

	override fun load(): List<Plugin> {
		val urls = pluginDetector.jars
		val loader = URLClassLoader(urls, javaClass.classLoader)
		val plugins = ServiceLoader.load(Plugin::class.java, loader).asIterable().toList()
		if (plugins.size > 1) {
			val names = plugins.map(Plugin::name)
			logger.info("Loaded Plugins: " + names.joinToString())
			if (names.size != names.distinct().size) {
				throw IllegalStateException("Two plugins with same name found!")
			}
		}
		return plugins
	}

}