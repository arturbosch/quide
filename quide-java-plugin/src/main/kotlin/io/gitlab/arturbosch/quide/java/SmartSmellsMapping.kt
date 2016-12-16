package io.gitlab.arturbosch.quide.java

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
class SmartSmellsMapping : SmellMapping<JavaCodeSmell>, Processor {

	private val logger = LogManager.getLogger(SmartSmellsMapping::class.java.simpleName)

	override fun injectionPoint(): ControlFlow.InjectionPoint = ControlFlow.InjectionPoint.AfterDetection

	override fun <U : UserData?> execute(data: U) {
		logger.info("not implemented")
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