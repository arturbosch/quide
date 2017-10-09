package io.gitlab.arturbosch.quide.java.mapping

import com.github.javaparser.JavaParser
import com.github.javaparser.ParseStart
import com.github.javaparser.ParserConfiguration
import com.github.javaparser.Providers
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.Node
import difflib.Chunk
import io.gitlab.arturbosch.quide.vcs.DiffTool
import io.gitlab.arturbosch.quide.vcs.SourceFile

/**
 * @author Artur Bosch
 */
class ASTDiffTool : DiffTool<JavaCodeSmellPatch> {

	private val javaParser = JavaParser(ParserConfiguration().setAttributeComments(false))

	override fun createPatchFor(oldFile: SourceFile, newFile: SourceFile): JavaCodeSmellPatch {
		val oldUnit = javaParser.parse(
				ParseStart.COMPILATION_UNIT,
				Providers.provider(oldFile.content()))
		val newUnit = javaParser.parse(
				ParseStart.COMPILATION_UNIT,
				Providers.provider(newFile.content()))

		return if (oldUnit.isSuccessful && newUnit.isSuccessful) {
			val diffPatch = textDiff(oldFile.content(), newFile.content())
			ASTDiffer(oldUnit.result.get(), newUnit.result.get(), diffPatch).patch()
		} else {
			NOOPPatch
		}
	}

	inner class ASTDiffer(private val oldUnit: CompilationUnit,
						  private val newUnit: CompilationUnit,
						  diffPatch: difflib.Patch<String>) {

		private val chunks = diffPatch.deltas
				.map { ASTChunk(it.type, it.original.astElements(oldUnit), it.revised.astElements(newUnit)) }

		private fun <T> Chunk<T>.astElements(unit: CompilationUnit): List<Node> {
			val filter = ElementsInRangeFilter(this)
			filter.visitBreadthFirst(unit)
			return filter.posToElement
		}

		fun patch() = ASTPatch(chunks, newUnit)

	}

}

