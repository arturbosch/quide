package io.gitlab.arturbosch.quide.vcs

import org.vcsreader.VcsChange
import org.vcsreader.VcsCommit
import java.nio.file.Path

data class QuideVersion(private val versionId: Int,
						private val commit: VcsCommit,
						private val projectPath: Path,
						private val relativePath: Path) : Versionable {

	private val changes = commit.changes
			.filter { it.isWithinRelativePath() }
			.map { change -> QuideFileChange(relativePath, change) }.toMutableList()

	private fun VcsChange.isWithinRelativePath() = projectPath.resolve(filePath).toString().contains(relativePath.toString()) ||
			projectPath.resolve(filePathBefore).toString().contains(relativePath.toString())

	private val revision = QuideRevision(commit)

	override fun versionNumber(): Int = versionId
	override fun revision(): Revision = revision
	override fun fileChanges(): List<FileChange> = changes

}
