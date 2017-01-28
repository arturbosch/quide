package io.gitlab.arturbosch.quide.java.mapping

import io.gitlab.arturbosch.quide.java.JavaCodeSmell
import io.gitlab.arturbosch.quide.mapping.SmellCompareStrategy
import io.gitlab.arturbosch.quide.vcs.Patch
import io.gitlab.arturbosch.quide.vcs.SourceFile

/**
 * @author Artur Bosch
 */
class ASTCompareStrategy : SmellCompareStrategy<JavaCodeSmell> {

	override fun matches(first: JavaCodeSmell, second: JavaCodeSmell): Boolean {
		return first.asXmlContent == second.asXmlContent
	}

	override fun patchSmell(smell: JavaCodeSmell, sourceFile: SourceFile, patch: Patch): JavaCodeSmell {
		return smell
	}
}