package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.quide.detection.Detector
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.smartsmells.api.DetectorFacade
import io.gitlab.arturbosch.smartsmells.config.DetectorConfig

/**
 * @author Artur Bosch
 */
class SmartSmellsTool : Detector<JavaSmellContainer> {

	override fun name(): String {
		return "SmartSmells"
	}

	override fun <U : UserData> execute(data: U): JavaSmellContainer {
		val projectPath = data.projectPath()
		val configPath = data.quideDirectory().configurationsDir().resolve("smartsmells.yml")
		val facade = DetectorFacade.fromConfig(DetectorConfig.load(configPath))
		val smellResult = facade.run(projectPath)
		return JavaSmellContainer(smellResult)
	}
}
