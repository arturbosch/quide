package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.quide.detection.Detector
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.smartsmells.api.DetectorFacade
import io.gitlab.arturbosch.smartsmells.api.SmellResult
import io.gitlab.arturbosch.smartsmells.config.DetectorConfig
import io.gitlab.arturbosch.smartsmells.out.XMLWriter
import java.nio.file.Files
import java.time.LocalDateTime

/**
 * @author Artur Bosch
 */
class SmartSmellsTool : Detector<JavaSmellContainer> {

	override fun name(): String {
		return "SmartSmells"
	}

	override fun <U : UserData> execute(data: U): JavaSmellContainer {
		val currentVersion = data.currentVersion()
		val lastVersion = data.lastVersion()
		if (!lastVersion.isPresent) {
			val configPath = data.quideDirectory().configurationsDir().resolve("smartsmells.yml")
			val facade = DetectorFacade.fromConfig(DetectorConfig.load(configPath))
			val projectPath = data.projectPath()
			val smellResult = facade.run(projectPath)

			if (!currentVersion.isPresent && !lastVersion.isPresent) {
				val outputPath = data.outputPath().orElse(data.projectPath())
				val xml = XMLWriter.toXml(smellResult)
				Files.write(outputPath.resolve("smartsmells_${LocalDateTime.now()}.xml"), xml.toByteArray())
			}

			return JavaSmellContainer(smellResult)
		}
		return JavaSmellContainer(SmellResult())
	}

}
