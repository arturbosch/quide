package io.gitlab.arturbosch.quide.crawler.cli

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.ParameterException
import io.gitlab.arturbosch.quide.platform.DirectoryPathConverter
import io.gitlab.arturbosch.quide.platform.ExistingPathConverter
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
object Args {
	@Parameter(names = arrayOf("--input", "-i"),
			description = "The input git list in a file.",
			converter = ExistingPathConverter::class)
	var input: Path? = null
	@Parameter(names = arrayOf("--output", "-o"),
			description = "The output report folder path.",
			converter = DirectoryPathConverter::class)
	var output: Path? = null
	@Parameter(names = arrayOf("--gui"), description = "Use graphical interface?")
	var withGUI: Boolean? = null

	var usage: String = ""
}

fun Array<String>.parse(): Args {
	with(JCommander()) {
		setProgramName("quide-crawler")
		addObject(Args)
		try {
			parse(*this@parse)
		} catch (_: ParameterException) {
			usage()
			System.exit(-1)
		}
		storeUsageMessage()
	}
	return Args
}

private fun JCommander.storeUsageMessage() {
	val sb = StringBuilder()
	usage(sb)
	Args.usage = sb.toString()
}