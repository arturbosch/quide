package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.quide.mapping.SmellCompareStrategy
import io.gitlab.arturbosch.quide.mapping.SmellMapping
import io.gitlab.arturbosch.quide.model.SmellContainer
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.quide.vcs.DiffTool
import io.gitlab.arturbosch.quide.vcs.Versionable

/**
 * @author Artur Bosch
 */
class SmartSmellsMapping : SmellMapping<JavaCodeSmell> {

	override fun <U : UserData?> execute(data: U) {

	}

	override fun compareAlgorithm(): SmellCompareStrategy<JavaCodeSmell> {
		throw UnsupportedOperationException("not implemented")
	}

	override fun diffTool(): DiffTool {
		throw UnsupportedOperationException("not implemented")
	}

	override fun map(versionable: Versionable?,
					 before: SmellContainer<JavaCodeSmell>?,
					 after: SmellContainer<JavaCodeSmell>?): SmellContainer<JavaCodeSmell> {
		throw UnsupportedOperationException("not implemented")
	}
}