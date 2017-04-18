package io.gitlab.arturbosch.quide.crawler.pipe

import io.gitlab.arturbosch.quide.crawler.Console
import org.vcsreader.vcs.git.GitVcsRoot

/**
 * @author Artur Bosch
 */
object Clone {

	fun git(fileName: String, project: GitVcsRoot) {
		val cloneResult = project.cloneToLocal()
		Console.write("Finished cloning $fileName...")
		if (!cloneResult.isSuccessful) {
			cloneResult.vcsErrors().forEach { Console.write(it) }
			throw PipeError("Error while cloning repo $fileName")
		}
	}
}