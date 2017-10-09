package io.gitlab.arturbosch.quide.crawler.pipe

import io.gitlab.arturbosch.quide.crawler.Console
import io.gitlab.arturbosch.quide.vcs.extractVcsErrors
import org.vcsreader.vcs.git.GitVcsRoot

/**
 * @author Artur Bosch
 */
object Clone {

	fun git(fileName: String, project: GitVcsRoot) {
		val cloneResult = project.cloneIt()
		if (!cloneResult.isSuccessful) {
			throw PipeError(extractVcsErrors(cloneResult.exceptions()).joinToString())
		}
		Console.write("Finished cloning $fileName...")
	}
}