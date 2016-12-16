package io.gitlab.arturbosch.quide.detekt

import io.gitlab.arturbosch.quide.mapping.SmellCompareStrategy
import io.gitlab.arturbosch.quide.mapping.SmellMapping
import io.gitlab.arturbosch.quide.model.SmellContainer
import io.gitlab.arturbosch.quide.platform.ControlFlow
import io.gitlab.arturbosch.quide.platform.Processor
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.quide.vcs.DiffTool
import io.gitlab.arturbosch.quide.vcs.Versionable
import org.apache.logging.log4j.LogManager

/**
 * @author Artur Bosch
 */
class DetektMapping : SmellMapping<DetektCodeSmell>, Processor {

	override fun injectionPoint(): ControlFlow.InjectionPoint = ControlFlow.InjectionPoint.AfterDetection

	private val logger = LogManager.getLogger(DetektMapping::class.java.simpleName)

	override fun <U : UserData?> execute(data: U) {
		logger.info("not implemented")
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