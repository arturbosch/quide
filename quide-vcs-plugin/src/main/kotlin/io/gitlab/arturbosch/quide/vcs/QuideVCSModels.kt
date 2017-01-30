package io.gitlab.arturbosch.quide.vcs

import io.gitlab.arturbosch.kutils.toZonedDateTime
import org.vcsreader.VcsChange
import org.vcsreader.VcsCommit
import java.nio.file.Path
import java.time.ZonedDateTime
import java.util.concurrent.atomic.AtomicInteger


private val idGenerator = AtomicInteger()
private val nextVersion: Int
	get() = idGenerator.incrementAndGet()

data class QuideVersion(private val commit: VcsCommit,
						private val relativePath: Path,
						private val versionId: Int = nextVersion) : Versionable {

	private val changes = commit.changes.map(::QuideFileChange).toMutableList()
	private val revision = QuideRevision(commit)

	override fun versionNumber(): Int = versionId
	override fun revision(): Revision = revision
	override fun fileChanges(): List<FileChange> = changes

}

data class QuideRevision(private val commit: VcsCommit) : Revision {
	override fun versionHash(): String = commit.revision
	override fun parentHash(): String = commit.revisionBefore
	override fun message(): String = commit.message
	override fun author(): String = commit.author
	override fun date(): ZonedDateTime = commit.time.toZonedDateTime()
	override fun isMerge(): Boolean = false
}

data class QuideSourceFile(private val path: String, private val content: String) : SourceFile {
	override fun path(): String = path
	override fun content(): String = content
}

data class QuideFileChange(private val vcsChange: VcsChange) : FileChange {

	private val type: FileChange.Type = when (vcsChange.type) {
		VcsChange.Type.ADDED -> FileChange.Type.ADDITION
		VcsChange.Type.DELETED -> FileChange.Type.REMOVAL
		VcsChange.Type.MODIFIED -> FileChange.Type.MODIFICATION
		VcsChange.Type.MOVED -> FileChange.Type.RELOCATION
		else -> throw UnsupportedOperationException("No other types are supported!")
	}

	private val oldFile = QuideSourceFile(vcsChange.filePathBefore, vcsChange.fileContentBefore().value)
	private val newFile = QuideSourceFile(vcsChange.filePath, vcsChange.fileContent().value)
	private var patch: Patch<*>? = null

	override fun type(): FileChange.Type = type
	override fun oldFile(): SourceFile = oldFile
	override fun newFile(): SourceFile = newFile

	@Suppress("UNCHECKED_CAST")
	override fun <P : Patch<*>?> patch(diff: DiffTool<P>): P {
		return patch as P ?: super.patch(diff).apply { patch = this }
	}
}