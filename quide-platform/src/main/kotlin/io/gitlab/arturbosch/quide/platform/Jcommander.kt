package io.gitlab.arturbosch.quide.platform

import com.beust.jcommander.IStringConverter
import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException
import io.gitlab.arturbosch.kutils.isDirectory
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
			converter = DirectoryPathConverter::class)
	var output: Path? = null
	@Parameter(names = arrayOf("--properties", "-p"), description = "Additional properties as key=value pairs.")
	var properties: String? = null
	@Parameter(names = arrayOf("--propertyPaths", "-pp"), description = "Additional property paths separated by comma's.")
	var propertyPaths: String? = null
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

	parseAdditionalProperties()

	return Args
}

private fun parseAdditionalProperties() {
	Args.properties?.let {
		HomeFolder.addPropertyPairs(it)
	}

	Args.propertyPaths?.let {
		HomeFolder.addPropertiesFromString(it)
	}
}

class ExistingPathConverter : IStringConverter<Path?> {
	override fun convert(value: String): Path? {
		val config = Paths.get(value)
		if (Files.notExists(config))
			throw ParameterException("Provided path '$value' does not exist!")
		return config
	}
}

class DirectoryPathConverter : IStringConverter<Path?> {
	override fun convert(value: String): Path? {
		return Paths.get(value).apply {
			Files.createDirectories(this)
			require(this.isDirectory()) { "Output path must be a directory to store all reports!" }
		}
	}

}