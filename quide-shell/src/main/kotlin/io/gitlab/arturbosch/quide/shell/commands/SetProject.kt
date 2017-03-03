package io.gitlab.arturbosch.quide.shell.commands

import io.gitlab.arturbosch.quide.shell.QuideState
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Artur Bosch
 */
class SetProject(val path: Path) : Commando {

	override fun run() {
		QuideState.projectPath = path
		println(path)
	}

	companion object : Parsable {
		override val id: String = "project"
		override fun parse(line: String) = SetProject(Paths.get(line.trim()))
	}

}