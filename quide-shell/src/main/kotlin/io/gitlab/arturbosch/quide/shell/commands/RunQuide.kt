package io.gitlab.arturbosch.quide.shell.commands

import io.gitlab.arturbosch.quide.platform.processors.ResultConsoleProcessor
import io.gitlab.arturbosch.quide.shell.QuideState

/**
 * @author Artur Bosch
 */
class RunQuide : Commando {

	override fun run() {
		val platform = QuideState.platform
		platform.analyze()
		val processor = ResultConsoleProcessor()
		platform.plugins().forEach {
			processor.execute(it.userData())
		}
	}

	companion object : Parsable {
		override val id: String = "run"
		override fun parse(line: String): Commando = RunQuide()

	}
}