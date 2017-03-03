package io.gitlab.arturbosch.quide.shell

import io.gitlab.arturbosch.quide.platform.BasePlatform
import io.gitlab.arturbosch.quide.platform.BasePluginDetector
import io.gitlab.arturbosch.quide.platform.BasePluginLoader
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

	val platform = BasePlatform(ConfigurableAnalysis, BasePluginLoader(BasePluginDetector))
}
