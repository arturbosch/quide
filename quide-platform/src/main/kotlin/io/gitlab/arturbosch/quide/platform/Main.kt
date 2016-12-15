package io.gitlab.arturbosch.quide.platform

import java.nio.file.Paths

/**
 * @author Artur Bosch
 */
fun main(args: Array<String>) {
	val analysis = Analysis(Paths.get(args[0]))
	Platform(BasePluginLoader(BasePluginDetector))
			.execute(analysis.projectPath)
}