package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.quide.detection.Detector
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.smartsmells.api.DetectorFacade

/**
 * @author Artur Bosch
 */
class SmartSmellsTool : Detector<JavaSmellContainer> {

	override fun name(): String {
		return "SmartSmells"
	}

	override fun <U : UserData> execute(data: U): JavaSmellContainer {
		val projectPath = data.projectPath()
		val smellResult = DetectorFacade.fullStackFacade().run(projectPath)
		return JavaSmellContainer(smellResult)
	}

}