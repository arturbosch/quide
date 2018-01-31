package io.gitlab.arturbosch.quide.platform.loaders

import io.gitlab.arturbosch.quide.api.Plugin
import mu.KLogging
import java.net.URLClassLoader
import java.util.ServiceLoader

/**
 * @author Artur Bosch
 */
class DefaultPluginLoader(
		private val pluginDetector: PluginDetector) : KLogging(), PluginLoader {

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
