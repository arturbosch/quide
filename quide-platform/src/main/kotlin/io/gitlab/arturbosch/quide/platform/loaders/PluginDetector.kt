package io.gitlab.arturbosch.quide.platform.loaders

import java.net.URL

/**
 * @author Artur Bosch
 */
interface PluginDetector {
	val jars: Array<URL>
}
