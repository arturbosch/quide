package io.gitlab.arturbosch.quide.crawler.pipe

import io.gitlab.arturbosch.quide.crawler.Console
import org.vcsreader.vcs.git.GitVcsRoot

/**
 * @author Artur Bosch
 */
object Clone {

	fun git(fileName: String, project: GitVcsRoot) {
		val cloneResult = project.cloneToLocal()
		if (!cloneResult.isSuccessful) {
			throw PipeError(cloneResult.vcsErrors().joinToString())
		}
		Console.write("Finished cloning $fileName...")
	}
}