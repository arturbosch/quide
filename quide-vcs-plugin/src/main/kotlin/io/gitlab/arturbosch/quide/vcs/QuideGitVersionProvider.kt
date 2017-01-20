package io.gitlab.arturbosch.quide.vcs

import org.slf4j.LoggerFactory
import org.vcsreader.VcsCommit
import org.vcsreader.VcsProject
import org.vcsreader.vcs.git.GitVcsRoot
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate
import java.util.Optional

/**
 * @author Artur Bosch
 */
class QuideGitVersionProvider(val root: Path = Paths.get("/home/artur/master/elasticsearch"),
							  val relative: Path = root.resolve("/src/main/java")) : VersionProvider {

	private val logger = LoggerFactory.getLogger(javaClass.simpleName)
	private val logResult: VcsProject.LogResult
	private val commits: List<VcsCommit>

	private var index: Int = 0
	private val maxIndex: Int

	init {
		val gitVcsRoot = GitVcsRoot(root.toString(), null)
		val vcsProject = VcsProject(gitVcsRoot)
		logger.info("Checking out commits...")
		logResult = vcsProject.log(LocalDate.of(2005, 1, 1).toDate(), LocalDate.of(2017, 1, 1).toDate())
		commits = logResult.commits()
		logger.info("Finished git log...")
		maxIndex = commits.size - 1
		assert(maxIndex > -1) { "There were no commits to analyze!!!" }
		logResult.vcsErrors().forEach(::println)
		logResult.exceptions().forEach(::println)
	}

	override fun nextVersion(): Optional<Versionable> {
		if (index <= maxIndex) {
			val commit = commits[index]
			logger.info("Transforming versions...")
			val version = QuideVersion(commit = commit, relativePath = relative)
			logger.info("Finish transforming...")
//			version.checkout()
			index++
			return Optional.of(version)
		}
		return Optional.empty()
	}

}