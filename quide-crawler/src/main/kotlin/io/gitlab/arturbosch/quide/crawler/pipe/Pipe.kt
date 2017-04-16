package io.gitlab.arturbosch.quide.crawler.pipe

import io.gitlab.arturbosch.kutils.asPath
import org.vcsreader.VcsProject
import org.vcsreader.vcs.git.GitVcsRoot
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
object Pipe {

	fun start(cloneRootDir: Path, gitEntries: List<String>) {
		val gits = produceGitRoots(cloneRootDir, gitEntries)
		val project = VcsProject(gits)
		val cloneResult = project.cloneToLocal()
		if (cloneResult.isSuccessful) {
			gits.forEach {
				Analyze.start(it.localPath.asPath())
			}
		} else {
			cloneResult.vcsErrors().forEach(::println)
		}
	}

	private fun produceGitRoots(cloneRootDir: Path, gitEntries: List<String>): List<GitVcsRoot> {
		return gitEntries.map {
			val name = it.substringAfter("/").substringBefore(".")
			GitVcsRoot(cloneRootDir.resolve(name).toString(), it)
		}
	}
}