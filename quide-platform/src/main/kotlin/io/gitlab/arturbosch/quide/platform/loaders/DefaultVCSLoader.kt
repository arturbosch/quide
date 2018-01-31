package io.gitlab.arturbosch.quide.platform.loaders

import io.gitlab.arturbosch.quide.vcs.VersionProvider
import mu.KLogging
import java.net.URLClassLoader
import java.util.ServiceLoader

/**
 * @author Artur Bosch
 */
class DefaultVCSLoader(
		private val pluginDetector: PluginDetector) : KLogging(), VCSLoader {

	override fun load(): VersionProvider? {
		val urls = pluginDetector.jars
		val loader = URLClassLoader(urls, javaClass.classLoader)
		return ServiceLoader.load(VersionProvider::class.java, loader)
				.asIterable().toList().firstOrNull().apply {
			if (this != null) {
				logger.info("Registered VersionProvider: " + this.name())
			}
		}
	}
}
