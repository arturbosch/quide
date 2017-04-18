package io.gitlab.arturbosch.quide.crawler.pipe

import io.gitlab.arturbosch.kutils.asPath
import io.gitlab.arturbosch.quide.crawler.Console
import org.vcsreader.vcs.git.GitVcsRoot

/**
 * @author Artur Bosch
 */
object Update {

	fun git(fileName: String, project: GitVcsRoot) {
		val result = project.update()
		if (!result.isSuccessful) {
			result.vcsErrors().forEach { Console.write(it) }
			throw PipeError("Error while updating repo ${project.localPath.asPath().fileName}")
		}
		Console.write("Updated existing repo $fileName...")
	}
}