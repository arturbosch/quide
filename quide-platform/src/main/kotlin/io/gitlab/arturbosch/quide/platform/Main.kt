package io.gitlab.arturbosch.quide.platform

/**
 * @author Artur Bosch
 */
fun main(args: Array<String>) {
	QuidePlatform(
			BaseVCSLoader(BasePluginDetector),
			BasePlatform(Analysis.parse(args), BasePluginLoader(BasePluginDetector))
	).analyze()
}


const val QUIDE = "quide"