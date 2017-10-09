package io.gitlab.arturbosch.quide.vcs

import org.vcsreader.VcsChange
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
data class QuideFileChange(private val path: Path, private val vcsChange: VcsChange) : FileChange {

	private val type: FileChange.Type = when (vcsChange.type) {
		VcsChange.Type.Added -> FileChange.Type.ADDITION
		VcsChange.Type.Deleted -> FileChange.Type.REMOVAL
		VcsChange.Type.Modified -> FileChange.Type.MODIFICATION
		VcsChange.Type.Moved -> FileChange.Type.RELOCATION
		else -> throw UnsupportedOperationException("No other types are supported!")
	}

	private val oldFile = QuideSourceFile(path.resolve(vcsChange.filePathBefore).toString()) {
		vcsChange.fileContentBefore().value
	}

	private val newFile = QuideSourceFile(path.resolve(vcsChange.filePath).toString()) {
		vcsChange.fileContent().value
	}

	private var patch: Patch<*>? = null

	override fun type(): FileChange.Type = type
	override fun oldFile(): SourceFile = oldFile
	override fun newFile(): SourceFile = newFile

	@Suppress("UNCHECKED_CAST")
	override fun <P : Patch<*>> patch(diff: DiffTool<P>): P {
		if (patch == null) {
			patch = super.patch(diff)
		}
		return patch as P
	}
}
