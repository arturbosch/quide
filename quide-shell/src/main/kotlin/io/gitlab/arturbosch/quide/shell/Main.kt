package io.gitlab.arturbosch.quide.shell

import org.jline.reader.EndOfFileException
import org.jline.reader.LineReaderBuilder
import org.jline.reader.UserInterruptException
import org.jline.terminal.TerminalBuilder

/**
 * @author Artur Bosch
 */

fun main(args: Array<String>) {
	val reader = LineReaderBuilder.builder()
			.appName("quide")
			.terminal(TerminalBuilder.terminal())
			.build()
	val prompt = "quide>"
	while (true) {
		var line: String?
		try {
			line = reader.readLine(prompt)
			println(line)
		} catch (e: UserInterruptException) {
			// Ignore
		} catch (e: EndOfFileException) {
			return
		}
	}
}