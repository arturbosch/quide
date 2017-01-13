package io.gitlab.arturbosch.quide.vcs

import org.vcsreader.VcsCommit
import org.vcsreader.VcsProject
import org.vcsreader.lang.CommandLine
import org.vcsreader.vcs.git.GitVcsRoot
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate
import java.util.Optional

/**
 * @author Artur Bosch
 */
class QuideGitVersionProvider(val root: Path = Paths.get("/home/artur/master/elasticsearch"),
							  val relative: Path = root.resolve("code/src/main/java")) : VersionProvider {

	private val logResult: VcsProject.LogResult
	private val commits: List<VcsCommit>

	private var index: Int = 0
	private val maxIndex: Int

	init {
		val gitVcsRoot = GitVcsRoot(root.toString(), null)
		val vcsProject = VcsProject(gitVcsRoot)
		logResult = vcsProject.log(LocalDate.of(2005, 1, 1).toDate(), LocalDate.of(2015, 1, 1).toDate())
		commits = logResult.commits()
		maxIndex = commits.size - 1
		assert(maxIndex > -1) { "There were no commits to analyze!!!" }
		logResult.vcsErrors().forEach(::println)
		logResult.exceptions().forEach(::println)
	}

	override fun nextVersion(): Optional<Versionable> {
		if (index <= maxIndex) {
			val commit = commits[index]
			val version = QuideVersion(commit = commit, relativePath = relative)
//			version.checkout()
			index++
			return Optional.of(version)
		}
		return Optional.empty()
	}

	private fun Versionable.checkout() {
		val git: CommandLine = object : CommandLine("git", "--git-dir=$root", "checkout", revision().versionHash()) {}
		git.execute()
		if (!git.isSuccessful()) throw RuntimeException("Git checkout was not successful! $this")
	}
}

fun CommandLine.isSuccessful(): Boolean {
	println(exitCode())
	println(exceptionStacktrace())
	// don't check stderr because git can use it for non-error information
	return exitCode() == 0 && hasNoExceptions()
}