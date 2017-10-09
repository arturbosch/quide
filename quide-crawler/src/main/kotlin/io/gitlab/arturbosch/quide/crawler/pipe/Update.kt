package io.gitlab.arturbosch.quide.crawler.pipe

import io.gitlab.arturbosch.quide.crawler.Console
import io.gitlab.arturbosch.quide.vcs.extractVcsErrors
import org.vcsreader.vcs.git.GitVcsRoot

/**
 * @author Artur Bosch
 */
object Update {

	fun git(fileName: String, project: GitVcsRoot) {
		val result = project.update()
		if (!result.isSuccessful) {
			throw PipeError(extractVcsErrors(result.exceptions()).joinToString())
		}
		Console.write("Updated existing repo $fileName...")
	}
}