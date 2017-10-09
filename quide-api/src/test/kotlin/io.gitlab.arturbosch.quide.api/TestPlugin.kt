package io.gitlab.arturbosch.quide.api

import io.gitlab.arturbosch.quide.api.processors.Processor
import io.gitlab.arturbosch.quide.model.CodeSmell
import io.gitlab.arturbosch.quide.model.SmellContainer

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

class KotlinTestCodeSmellDetector : Detector<SmellContainer<CodeSmell>> {
	override fun execute(context: AnalysisContext): SmellContainer<CodeSmell> {
		return SmellContainer { mutableListOf<CodeSmell>() }
	}
}