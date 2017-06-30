package io.gitlab.arturbosch.quide.api

import io.gitlab.arturbosch.quide.api.processors.Processor
import io.gitlab.arturbosch.quide.model.CodeSmell
import io.gitlab.arturbosch.quide.model.SmellContainer

/**
 * @author Artur Bosch
 */
interface Plugin : Nameable {

	val id get() = name
	fun define(context: Context)

	class Context {

		var detectorInstance: Detector<*>? = null
			private set
		val registeredProcessors get() = _registeredProcessors.toSet()
		private val _registeredProcessors: MutableSet<Processor> = mutableSetOf()

		fun <T : CodeSmell, C : SmellContainer<T>> register(detector: Detector<C>) {
			if (detectorInstance != null) {
				throw IllegalArgumentException("A detector was already registered for this plugin!")
			}
			detectorInstance = detector
		}

		fun register(vararg processors: Processor) {
			_registeredProcessors.addAll(processors)
		}
	}
}