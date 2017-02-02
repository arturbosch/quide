package io.gitlab.arturbosch.quide.java.mapping

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.Node
import difflib.Chunk
import io.gitlab.arturbosch.quide.vcs.DiffTool
import io.gitlab.arturbosch.quide.vcs.SourceFile

/**
 * @author Artur Bosch
 */
class ASTDiffTool : DiffTool<ASTPatch> {

	override fun createPatchFor(oldFile: SourceFile, newFile: SourceFile): ASTPatch {
		val oldUnit = JavaParser.parse(oldFile.content())
		val newUnit = JavaParser.parse(newFile.content())
		val diffPatch = textDiff(oldFile.content(), newFile.content())
		diffPatch.deltas.forEach(::println)
		return ASTDiffer(oldUnit, newUnit, diffPatch).patch().apply {
			chunks.forEach(::println)
		}
	}

	inner class ASTDiffer(private val oldUnit: CompilationUnit,
						  private val newUnit: CompilationUnit,
						  diffPatch: difflib.Patch<String>) {


		val chunks = diffPatch.deltas
				.map { ASTChunk(it.type, it.original.astElements(oldUnit), it.revised.astElements(newUnit)) }

		private fun <T> Chunk<T>.astElements(unit: CompilationUnit): List<Node> {
			val filter = ElementsInRangeFilter(this)
			filter.visitBreadthFirst(unit)
			return filter.posToElement
		}

		fun patch(): ASTPatch {
			return ASTPatch(chunks, newUnit)
		}

	}

}

