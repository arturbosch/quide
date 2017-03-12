package io.gitlab.arturbosch.quide.platform

/**
 * @author Artur Bosch
 */
fun main(args: Array<String>) {
	val analysis = Analysis.parse(args)
	QuidePlatform(analysis,
			BaseVCSLoader(BasePluginDetector, analysis),
			BasePluginLoader(BasePluginDetector)
	).analyze()
}

const val QUIDE = "quide"
