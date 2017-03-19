package io.gitlab.arturbosch.quide.java.core

import io.gitlab.arturbosch.quide.detection.Detector
import io.gitlab.arturbosch.quide.java.JavaPluginData
import io.gitlab.arturbosch.quide.platform.QuideConstants
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.quide.vcs.Versionable
import io.gitlab.arturbosch.smartsmells.api.SmellResult
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
		val pluginData = data as JavaPluginData
		val currentVersion = data.currentVersion()
		val lastVersion = data.lastVersion()
		val projectPath = pluginData.projectPath()

		val smellResult = if (pluginData.isEvolutionaryAnalysis()) {
			val updatableFacade = pluginData.updatableFacade()
			val versionable = currentVersion.orElseThrow { IllegalStateException("In evolutionary analysis but no version?") }
			VersionCrawler(projectPath, versionable, updatableFacade).run()
		} else {
			pluginData.facade().run(projectPath)
		}

		if (isReportWanted(currentVersion, lastVersion)) {
			generateReport(data, smellResult)
		}
		return JavaSmellContainer(smellResult)
	}

	private fun isReportWanted(currentVersion: Optional<Versionable>, lastVersion: Optional<Versionable>) =
			!currentVersion.isPresent && !lastVersion.isPresent

	private fun <U : UserData> generateReport(data: U, smellResult: SmellResult) {
		if (skipResultFile(data)) return

		val outputPath = data.outputPath().orElse(data.projectPath())
		val xml = XMLWriter.toXml(smellResult)
		Files.write(outputPath.resolve("smartsmells_${LocalDateTime.now()}.xml"), xml.toByteArray())
	}

	private fun <U : UserData> skipResultFile(data: U): Boolean {
		val property = data.quideDirectory().getProperty(QuideConstants.OUTPUT_FILE)
		return !property.toBoolean()
	}

}
