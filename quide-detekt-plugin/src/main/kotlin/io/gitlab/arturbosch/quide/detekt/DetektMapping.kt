package io.gitlab.arturbosch.quide.detekt

import io.gitlab.arturbosch.quide.mapping.SmellCompareStrategy
import io.gitlab.arturbosch.quide.mapping.SmellMapping
import io.gitlab.arturbosch.quide.model.SmellContainer
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.quide.vcs.DiffTool
import io.gitlab.arturbosch.quide.vcs.Versionable

/**
 * @author Artur Bosch
 */
class DetektMapping : SmellMapping<DetektCodeSmell>{

	override fun <U : UserData?> execute(data: U) {

	}

	override fun compareAlgorithm(): SmellCompareStrategy<DetektCodeSmell> {
		throw UnsupportedOperationException("not implemented")
	}

	override fun diffTool(): DiffTool {
		throw UnsupportedOperationException("not implemented")
	}

	override fun map(versionable: Versionable?, before: SmellContainer<DetektCodeSmell>?, after: SmellContainer<DetektCodeSmell>?): SmellContainer<DetektCodeSmell> {
		throw UnsupportedOperationException("not implemented")
	}
}