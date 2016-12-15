package io.gitlab.arturbosch.quide.platform

import java.net.URLClassLoader
import java.util.ServiceLoader

/**
 * @author Artur Bosch
 */
class PluginLoader(val pluginDetector: PluginDetector) {

	fun load(): List<Plugin> {
		val urls = pluginDetector.search()
				.toTypedArray()
		val loader = URLClassLoader(urls)
		return ServiceLoader.load(Plugin::class.java, loader)
				.asIterable().toList()
	}

}