package io.gitlab.arturbosch.quide.vcs

import org.vcsreader.VcsChange
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
data class QuideFileChange(private val path: Path, private val vcsChange: VcsChange) : FileChange {

	private val type: FileChange.Type = when (vcsChange.type) {
		VcsChange.Type.ADDED -> FileChange.Type.ADDITION
		VcsChange.Type.DELETED -> FileChange.Type.REMOVAL
		VcsChange.Type.MODIFIED -> FileChange.Type.MODIFICATION
		VcsChange.Type.MOVED -> FileChange.Type.RELOCATION
		else -> throw UnsupportedOperationException("No other types are supported!")
	}

	private val oldFile = QuideSourceFile(path.resolve(vcsChange.filePathBefore).toString()) { vcsChange.fileContentBefore().value }
	private val newFile = QuideSourceFile(path.resolve(vcsChange.filePath).toString()) { vcsChange.fileContent().value }
	private var patch: Patch<*>? = null

	override fun type(): FileChange.Type = type
	override fun oldFile(): SourceFile = oldFile
	override fun newFile(): SourceFile = newFile

	@Suppress("UNCHECKED_CAST")
	override fun <P : Patch<*>?> patch(diff: DiffTool<P>): P {
		return patch as P ?: super.patch(diff).apply { patch = this }
	}
}