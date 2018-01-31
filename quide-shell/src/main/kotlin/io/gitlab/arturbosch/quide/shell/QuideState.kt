package io.gitlab.arturbosch.quide.shell

import java.nio.file.Path

/**
 * @author Artur Bosch
 */
object QuideState {

	const val DEFAULT_PROMPT = "quide"

	var prompt: String = DEFAULT_PROMPT + ">"
	var projectPath: Path? = null
		set(value) {
			prompt = DEFAULT_PROMPT + "@" + value?.fileName + ">"
			field = value
		}
}
