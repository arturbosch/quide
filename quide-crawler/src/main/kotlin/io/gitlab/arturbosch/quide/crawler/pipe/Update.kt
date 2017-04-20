package io.gitlab.arturbosch.quide.crawler.pipe

import io.gitlab.arturbosch.quide.crawler.Console
import org.vcsreader.vcs.git.GitVcsRoot

/**
 * @author Artur Bosch
 */
object Update {

	fun git(fileName: String, project: GitVcsRoot) {
		val result = project.update()
		if (!result.isSuccessful) {
			throw PipeError(result.vcsErrors().joinToString())
		}
		Console.write("Updated existing repo $fileName...")
	}
}