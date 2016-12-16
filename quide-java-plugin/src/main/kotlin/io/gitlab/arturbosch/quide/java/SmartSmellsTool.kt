package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.quide.detection.Detector
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.smartsmells.api.DetectorFacade

/**
 * @author Artur Bosch
 */
class SmartSmellsTool : Detector<JavaCodeSmell> {

	override fun name(): String {
		return "SmartSmells"
	}

	override fun <U : UserData> execute(data: U) {
		data.projectPath().ifPresent {
			val smellResult = DetectorFacade.fullStackFacade().run(it)
			val container = JavaSmellContainer(smellResult)
			data.put("currentContainer", container)
		}
	}

}