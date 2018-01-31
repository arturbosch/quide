package io.gitlab.arturbosch.quide.platform

import io.gitlab.arturbosch.quide.platform.analysis.Platform
import io.gitlab.arturbosch.quide.platform.cli.parseArguments
import io.gitlab.arturbosch.quide.platform.loaders.DefaultPluginDetector
import io.gitlab.arturbosch.quide.platform.loaders.DefaultPluginLoader
import io.gitlab.arturbosch.quide.platform.loaders.DefaultVCSLoader

/**
 * @author Artur Bosch
 */
fun main(args: Array<String>) {
	val arguments = args.parseArguments()
	val plugins = DefaultPluginLoader(DefaultPluginDetector).load()
	val provider = DefaultVCSLoader(DefaultPluginDetector).load()

	if (provider != null) {
		throw IllegalStateException("Evolutionary analysis is not ready yet!")
	}

	Platform(arguments, plugins).run()
}

const val QUIDE = "quide"
