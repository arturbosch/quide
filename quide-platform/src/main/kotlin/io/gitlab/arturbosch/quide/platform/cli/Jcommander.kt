package io.gitlab.arturbosch.quide.platform.cli

import com.beust.jcommander.JCommander
import com.beust.jcommander.ParameterException
import io.gitlab.arturbosch.quide.platform.HomeFolder
import io.gitlab.arturbosch.quide.platform.QUIDE

private val jCommander = JCommander()

class JcommanderInitializationFailure : IllegalArgumentException("")

fun Array<String>.parseArguments(): Args {
	jCommander.programName = QUIDE
	val args = Args()
	jCommander.addObject(args)

	try {
		jCommander.parse(*this)
	} catch (ex: ParameterException) {
		println(ex.message)
		println()
		jCommander.usage()
		System.exit(-1)
	}

	if (args.help) {
		jCommander.usage()
		System.exit(-1)
	}

	args.properties?.let { HomeFolder.addPropertyPairs(it) }
	args.propertyPaths?.let { HomeFolder.addPropertiesFromString(it) }

	return args
}
