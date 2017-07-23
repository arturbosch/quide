package io.gitlab.arturbosch.quide.vcs

import io.gitlab.arturbosch.kutils.toDate
import io.gitlab.arturbosch.quide.platform.AnalysisContext
import io.gitlab.arturbosch.quide.platform.QuideConstants
import org.slf4j.LoggerFactory
import org.vcsreader.VcsCommit
import org.vcsreader.VcsProject
import org.vcsreader.lang.TimeRange
import org.vcsreader.vcs.git.GitVcsRoot
import java.nio.file.Path
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Optional

/**
 * @author Artur Bosch
 */
class QuideGitVersionProvider(var root: Path? = null,
							  var relative: Path? = root) : VersionProvider {

	private val logger = LoggerFactory.getLogger(javaClass.simpleName)

	private var index: Int = 0
	private val format = DateTimeFormatter.ISO_LOCAL_DATE_TIME
	private var since = LocalDate.of(2005, 1, 1).toDate()
	private var until = LocalDate.now().toDate()

	override fun initialize(context: AnalysisContext) {
		val quide = context.quideDirectory()

		root = context.projectPath()
		val relativePath = quide.getProperty(QuideConstants.VCS_RELATIVE_PATH)
		relative = relativePath?.let { root?.resolve(relativePath) } ?: root

		quide.getProperty(QuideConstants.VCS_START_COMMIT)?.let {
			index = it.toInt() - 1 // for array index
		}
		quide.getProperty(QuideConstants.VCS_RANGE_SINCE)?.let {
			since = LocalDateTime.from(format.parse(it)).toDate()
		}
		quide.getProperty(QuideConstants.VCS_RANGE_UNTIL)?.let {
			until = LocalDateTime.from(format.parse(it)).toDate()
		}
		logger.info("VCS configured to $relative.")
	}

	private val commits: List<VcsCommit> by lazy {
		assert(root != null) { "VCS root was not initialized!" }
		val gitVcsRoot = GitVcsRoot(root.toString(), null)
		val vcsProject = VcsProject(gitVcsRoot)
		logger.info("Checking out commits...")
		val logResult = vcsProject.log(TimeRange(since, until))
		val commits = logResult.commits()
		assert(commits.size - 1 > -1) { "There were no commits to analyze!!!" }
		logResult.exceptions().forEach { logger.error(it.message) }
		commits
	}

	override fun nextVersion(): Optional<Versionable> {
		if (index <= commits.size - 1) {
			val commit = commits[index]
			val version = QuideVersion(index + 1, commit, root!!, relative!!)
			index++
			return Optional.of(version)
		}
		return Optional.empty()
	}

}