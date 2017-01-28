package io.gitlab.arturbosch.quide.java.mapping

import io.gitlab.arturbosch.quide.vcs.DiffTool
import io.gitlab.arturbosch.quide.vcs.SourceFile

/**
 * @author Artur Bosch
 */
class ASTDiffTool : DiffTool<ASTPatch> {

	override fun createPatchFor(oldFile: SourceFile?, newFile: SourceFile?): ASTPatch {
		throw UnsupportedOperationException("not implemented")
	}
}