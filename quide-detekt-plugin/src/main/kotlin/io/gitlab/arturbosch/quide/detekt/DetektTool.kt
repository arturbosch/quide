package io.gitlab.arturbosch.quide.detekt

import io.gitlab.arturbosch.detekt.core.Detekt
import io.gitlab.arturbosch.quide.detection.Detector
import io.gitlab.arturbosch.quide.platform.UserData

/**
 * @author Artur Bosch
 */
class DetektTool : Detector<DetektCodeSmell> {

	override fun name(): String {
		return "Detekt"
	}

	override fun <U : UserData> execute(data: U) {
		data.projectPath().ifPresent {
			val detektion = Detekt(it).run()
			val smells = detektion.findings
					.flatMap { it.value }
					.map(::DetektCodeSmell)
			data.put("currentContainer", smells)
		}
	}

}
