package io.gitlab.arturbosch.quide.java.core

import io.gitlab.arturbosch.quide.platform.ControlFlow
import io.gitlab.arturbosch.quide.platform.Processor
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
class ResultXmlProcessor : Processor {

	override fun <U : UserData> execute(data: U) {
		val currentVersion = data.currentVersion()
		val lastVersion = data.lastVersion()
		data.safeContainer().ifPresent {
			if (isReportWanted(currentVersion, lastVersion)) {
				generateReport(data, it.smells)
			}
		}
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


	override fun injectionPoint(): ControlFlow.InjectionPoint {
		return ControlFlow.InjectionPoint.AfterAnalysis
	}

	override fun priority(): Int {
		return 100
	}
}