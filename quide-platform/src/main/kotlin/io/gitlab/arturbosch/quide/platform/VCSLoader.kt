package io.gitlab.arturbosch.quide.platform

import io.gitlab.arturbosch.quide.vcs.VersionProvider
import java.net.URLClassLoader
import java.util.ServiceLoader

/**
 * @author Artur Bosch
 */
interface VCSLoader {
	fun load(): VersionProvider?
}

class BaseVCSLoader(val pluginDetector: PluginDetector) : VCSLoader {

	private val logger by logFactory()

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