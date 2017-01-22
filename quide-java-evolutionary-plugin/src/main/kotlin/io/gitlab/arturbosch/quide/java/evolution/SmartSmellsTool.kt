package io.gitlab.arturbosch.quide.java.evolution

import io.gitlab.arturbosch.quide.detection.Detector
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.smartsmells.api.DetectorFacade
import io.gitlab.arturbosch.smartsmells.api.UpdatableDetectorFacade
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
		val updatableDetectorFacade = UpdatableDetectorFacade(projectPath, facade)

		val smellResult = VersionCrawler(projectPath, updatableDetectorFacade).loop()!!

		return JavaSmellContainer(smellResult)
	}


}
