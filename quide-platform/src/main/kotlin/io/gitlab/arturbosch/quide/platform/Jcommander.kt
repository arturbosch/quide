package io.gitlab.arturbosch.quide.platform

import com.beust.jcommander.IStringConverter
import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Artur Bosch
 */

object Args {
	@Parameter(names = arrayOf("--input", "-i"),
			required = true,
			description = "The input project path.",
			converter = ExistingPathConverter::class)
	lateinit var input: Path
	@Parameter(names = arrayOf("--output", "-o"),
			description = "The output report folder path.",
			converter = ExistingPathConverter::class)
	var output: Path? = null
	@Parameter(names = arrayOf("--help", "-h"), help = true, description = "Prints the help message.")
	var help = false
}

private val jCommander = JCommander()

fun parseArguments(args: Array<String>): Args {
	jCommander.setProgramName(QUIDE)
	jCommander.addObject(Args)

	try {
		jCommander.parse(*args)
	} catch (ex: ParameterException) {
		println(ex.message)
		println()
		jCommander.usage()
		System.exit(-1)
	}

	if (Args.help) {
		jCommander.usage()
		System.exit(-1)
	}

	return Args
}

class ExistingPathConverter : IStringConverter<Path?> {
	override fun convert(value: String): Path? {
		val config = Paths.get(value)
		if (Files.notExists(config))
			throw ParameterException("Provided path '$value' does not exist!")
		return config
	}
}