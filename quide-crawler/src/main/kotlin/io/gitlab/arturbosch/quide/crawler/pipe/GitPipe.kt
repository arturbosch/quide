package io.gitlab.arturbosch.quide.crawler.pipe

import io.gitlab.arturbosch.kutils.Try
import io.gitlab.arturbosch.kutils.asPath
import io.gitlab.arturbosch.kutils.exists
import io.gitlab.arturbosch.quide.crawler.Console
import io.gitlab.arturbosch.quide.crawler.cli.CrawlerOptions
import org.vcsreader.VcsProject
import org.vcsreader.vcs.git.GitVcsRoot
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
class GitPipe(val options: CrawlerOptions) : Pipe {

	override fun start(cloneRootDir: Path, entries: List<String>) {
		val gits = produceGitRoots(cloneRootDir, entries)
		VcsProject(gits) // sets default observers, prevent NPE
		gits.parallelStream().forEach { handleProject(it) }
	}

	private fun produceGitRoots(cloneRootDir: Path, gitEntries: List<String>): List<GitVcsRoot> {
		return gitEntries.map {
			val name = it.substringAfter("/").substringBefore(".")
			GitVcsRoot(cloneRootDir.resolve(name).toString(), it)
		}
	}

	private fun handleProject(project: GitVcsRoot) {
		val filePath = project.localPath.asPath()
		val fileName = filePath.fileName.toString()

		Try {
			if (filePath.exists()) {
				Update.git(fileName, project)
			} else {
				Clone.git(fileName, project)
			}
		} onSuccess {
			optionalPreAnalysis(filePath, fileName)
		} onError {
			Console.write("Error for $fileName: \n${it.message}")
		}
	}

	private fun optionalPreAnalysis(filePath: Path, fileName: String) {
		if (options.containsOperations(ANALYZE)) {
			Console.write("Collect metrics for $fileName...")
			Analyze.start(filePath)
		}
	}
}

const val ANALYZE = "ANALYZE"