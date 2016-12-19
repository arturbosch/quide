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
		val urls = pluginDetector.search().toTypedArray()
		val loader = URLClassLoader(urls, javaClass.classLoader)
		return ServiceLoader.load(Plugin::class.java, loader)
				.asIterable().toList().apply {
			logger.info("Loaded Plugins: " + joinToString(transform = Plugin::name))
		}
	}

}