package io.gitlab.arturbosch.quide.vcs

import io.gitlab.arturbosch.kutils.toDate
import io.gitlab.arturbosch.quide.platform.AnalysisContext
import org.slf4j.LoggerFactory
import org.vcsreader.VcsCommit
import org.vcsreader.VcsProject
import org.vcsreader.vcs.git.GitVcsRoot
import java.nio.file.Path
import java.time.LocalDate
import java.util.Optional

/**
 * @author Artur Bosch
 */
class QuideGitVersionProvider(var root: Path? = null,
							  var relative: Path? = root) : VersionProvider {

	private val logger = LoggerFactory.getLogger(javaClass.simpleName)

	private var index: Int = 0

	override fun initialize(context: AnalysisContext) {
		this.root = context.projectPath()
		val relativePath = context.quideDirectory().getProperty("vcs.relative.path")
		this.relative = relativePath?.let { root?.resolve(relativePath) } ?: root
	}

	private val since = LocalDate.of(2005, 1, 1).toDate()
	private val until = LocalDate.now().toDate()

	private val commits: List<VcsCommit> by lazy {
		assert(root != null) { "VCS root was not initialized!" }
		val gitVcsRoot = GitVcsRoot(root.toString(), null)
		val vcsProject = VcsProject(gitVcsRoot)
		logger.info("Checking out commits...")
		val logResult = vcsProject.log(since, until)
		val commits = logResult.commits()
		assert(commits.size - 1 > -1) { "There were no commits to analyze!!!" }
		logResult.vcsErrors().forEach(::println)
		logResult.exceptions().forEach(::println)
		commits
	}

	override fun nextVersion(): Optional<Versionable> {
		if (index <= commits.size - 1) {
			val commit = commits[index]
			val version = QuideVersion(commit = commit, projectPath = root!!, relativePath = relative!!)
			index++
			return Optional.of(version)
		}
		return Optional.empty()
	}

}