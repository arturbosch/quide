package io.gitlab.arturbosch.quide.api

import io.gitlab.arturbosch.quide.model.CodeSmell
import io.gitlab.arturbosch.quide.model.SmellContainer

/**
 * @author Artur Bosch
 */
interface Plugin : Nameable {

	fun define(context: Context) {
		context.register(MyCodeSmellDetector())
	}

	object Context {

		private var detectorInstance: Detector<*>? = null
		private val registeredProcessors: MutableList<Processor> = mutableListOf()

		fun <T : CodeSmell, C : SmellContainer<T>> register(detector: Detector<C>) {
			if (detectorInstance != null) {
				throw IllegalArgumentException("A detector was already registered for this plugin!")
			}
			detectorInstance = detector
		}

		fun register(vararg processors: Processor) {
			registeredProcessors.addAll(processors)
		}

	}
}