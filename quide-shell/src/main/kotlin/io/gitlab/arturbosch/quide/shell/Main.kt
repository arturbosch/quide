package io.gitlab.arturbosch.quide.shell

import io.gitlab.arturbosch.quide.platform.HomeFolder
import io.gitlab.arturbosch.quide.platform.QuideConstants
import io.gitlab.arturbosch.quide.shell.loaders.DefaultCommandLoader
import io.gitlab.arturbosch.quide.shell.loaders.JavaCommandLoader
import org.jline.reader.EndOfFileException
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.reader.UserInterruptException
import org.jline.terminal.TerminalBuilder

/**
 * @author Artur Bosch
 */

fun main(args: Array<String>) {
	parseArguments(args)
	overrideOutputProperties()
	val reader = reader()
	val commander = Commander(DefaultCommandLoader, JavaCommandLoader())
	while (true) {
		var line: String?
		try {
			line = reader.readLine(QuideState.prompt)
			commander.choose(line)
		} catch (e: QuideShellException) {
			e.message?.let { println(e.message) }
			e.cause?.let { println(e.cause) }
		} catch (e: UserInterruptException) {
			// Ignore
		} catch (e: EndOfFileException) {
			return
		}
	}
}

private fun overrideOutputProperties() {
	HomeFolder.addPropertyPairs("${QuideConstants.OUTPUT_CONSOLE}=false,${QuideConstants.OUTPUT_FILE}=false")
}

private fun reader(): LineReader {
	return LineReaderBuilder.builder()
			.appName(QuideState.DEFAULT_PROMPT)
			.terminal(TerminalBuilder.terminal())
			.build()
}