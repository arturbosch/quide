package io.gitlab.arturbosch.quide.java.mapping

import io.gitlab.arturbosch.quide.vcs.DiffTool
import io.gitlab.arturbosch.quide.vcs.Patch
import io.gitlab.arturbosch.quide.vcs.SourceFile

/**
 * @author Artur Bosch
 */
class ASTDiffTool : DiffTool {
	override fun createPatchFor(oldFile: SourceFile?, newFile: SourceFile?): Patch {
		throw UnsupportedOperationException("not implemented")
	}
}