package io.gitlab.arturbosch.quide.platform

/**
 * @author Artur Bosch
 */
fun main(args: Array<String>) {
	val analysis = Analysis.parse(args)
	QuidePlatform(
			BaseVCSLoader(BasePluginDetector, analysis),
			BasePlatform(analysis, BasePluginLoader(BasePluginDetector))
	).analyze()
}

const val QUIDE = "quide"