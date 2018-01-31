package io.gitlab.arturbosch.quide

import io.gitlab.arturbosch.quide.api.AnalysisContext
import io.gitlab.arturbosch.quide.api.Detector
import io.gitlab.arturbosch.quide.api.Plugin
import io.gitlab.arturbosch.quide.api.processors.Processor

/**
 * @author Artur Bosch
 */
object TestPlugin : Plugin {

	override val id: String = "test"

	override fun define(context: Plugin.Context) {
		context.apply {
			register(KotlinTestCodeSmellDetector())
			register(KotlinTestProcessor(), KotlinPrintTestProcessor())
		}
	}
}

class KotlinTestProcessor : Processor {
	override fun execute(context: AnalysisContext) {
		context.toString()
	}
}

class KotlinPrintTestProcessor : Processor {
	override fun execute(context: AnalysisContext) {
		println("Hello Quide from Kotlin!")
	}

}

class KotlinTestCodeSmellDetector : Detector {
	override fun execute(context: AnalysisContext) {}
}
