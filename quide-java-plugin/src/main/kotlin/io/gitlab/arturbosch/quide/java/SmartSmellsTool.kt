package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.quide.detection.Detector
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.quide.vcs.Versionable
import io.gitlab.arturbosch.smartsmells.api.DetectorFacade
import io.gitlab.arturbosch.smartsmells.api.SmellResult
import io.gitlab.arturbosch.smartsmells.api.UpdatableDetectorFacade
import io.gitlab.arturbosch.smartsmells.config.DetectorConfig
import io.gitlab.arturbosch.smartsmells.out.XMLWriter
import java.nio.file.Files
import java.time.LocalDateTime
import java.util.Optional

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
		val smellResult = if (!lastVersion.isPresent) {
			firstVersion(data)
		} else {
			nextVersion(currentVersion, data)
		}
		if (isReportWanted(currentVersion, lastVersion)) {
			generateReport(data, smellResult)
		}
		return JavaSmellContainer(smellResult)
	}

	private fun <U : UserData> generateReport(data: U, smellResult: SmellResult) {
		val outputPath = data.outputPath().orElse(data.projectPath())
		val xml = XMLWriter.toXml(smellResult)
		Files.write(outputPath.resolve("smartsmells_${LocalDateTime.now()}.xml"), xml.toByteArray())
	}

	private fun isReportWanted(currentVersion: Optional<Versionable>, lastVersion: Optional<Versionable>) = !currentVersion.isPresent && !lastVersion.isPresent

	private fun <U : UserData> firstVersion(data: U): SmellResult {

		val configPath = data.quideDirectory().configurationsDir().resolve("smartsmells.yml")
		val facade = DetectorFacade.fromConfig(DetectorConfig.load(configPath))
		val projectPath = data.projectPath()
		return facade.run(projectPath)
	}

	private fun <U : UserData> nextVersion(currentVersion: Optional<Versionable>, data: U): SmellResult {
		assert(currentVersion.isPresent)

		val projectPath = data.projectPath()
		val configPath = data.quideDirectory().configurationsDir().resolve("smartsmells.yml")

		val facade = DetectorFacade.fromConfig(DetectorConfig.load(configPath))
		val updatableDetectorFacade = UpdatableDetectorFacade(projectPath, facade)

		return VersionCrawler(projectPath, currentVersion.get(), updatableDetectorFacade).run()
	}

}
