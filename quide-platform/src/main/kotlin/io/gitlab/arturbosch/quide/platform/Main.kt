package io.gitlab.arturbosch.quide.platform

import java.nio.file.Paths

/**
 * @author Artur Bosch
 */
fun main(args: Array<String>) {
	val analysis = Analysis(Paths.get(args[0]))
	val detector = BasePluginDetector
	val quide = Quide(BaseVCSLoader(detector), BasePlatform(BasePluginLoader(detector)))
	quide.analyze(analysis.projectPath)
}

const val QUIDE = "quide"