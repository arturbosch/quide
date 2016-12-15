package io.gitlab.arturbosch.quide.detekt

import io.gitlab.arturbosch.quide.platform.ControlFlow
import io.gitlab.arturbosch.quide.platform.Processor
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.quide.platform.reflect.TypeToken

/**
 * @author Artur Bosch
 */
class PrintProcessor : Processor {

	override fun <U : UserData> execute(data: U) {
		data["currentContainer", TypeToken.get(DetektSmellContainer::class.java)].orElseThrow {
			RuntimeException("Could not retrieve container!")
		}.all().forEach {
			println(it.toString())
		}
	}

	override fun injectionPoint(): ControlFlow.InjectionPoint {
		return ControlFlow.InjectionPoint.AfterAnalysis
	}
}