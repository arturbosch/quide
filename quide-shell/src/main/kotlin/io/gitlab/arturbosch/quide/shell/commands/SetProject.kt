package io.gitlab.arturbosch.quide.shell.commands

import io.gitlab.arturbosch.quide.shell.QuideState
import java.nio.file.Paths

/**
 * @author Artur Bosch
 */
class SetProject : Command {

	override val id: String = "project"

	override fun run(line: String): String {
		val path = Paths.get(line.trim())
		QuideState.projectPath = path
		return "Project path set to: " + path
	}

}