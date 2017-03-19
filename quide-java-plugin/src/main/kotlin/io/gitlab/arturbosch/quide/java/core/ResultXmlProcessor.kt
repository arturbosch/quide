package io.gitlab.arturbosch.quide.java.core

import io.gitlab.arturbosch.quide.java.OUTPUT_JAVA_XML
import io.gitlab.arturbosch.quide.java.safeContainer
import io.gitlab.arturbosch.quide.platform.ControlFlow
import io.gitlab.arturbosch.quide.platform.Processor
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.smartsmells.api.SmellResult
import io.gitlab.arturbosch.smartsmells.out.XMLWriter
import java.nio.file.Files
import java.time.LocalDateTime

/**
 * @author Artur Bosch
 */
class ResultXmlProcessor : Processor {

	override fun <U : UserData> execute(data: U) {
		data.safeContainer().filter { data.isReportWanted() }
				.ifPresent { generateReport(data, it.smells) }
	}

	private fun UserData.isReportWanted() = quideDirectory().getProperty(OUTPUT_JAVA_XML).toBoolean()

	private fun <U : UserData> generateReport(data: U, smellResult: SmellResult) {
		val outputPath = data.outputPath().orElse(data.projectPath())
		val xml = XMLWriter.toXml(smellResult)
		Files.write(outputPath.resolve("smartsmells_${LocalDateTime.now()}.xml"), xml.toByteArray())
	}

	override fun injectionPoint(): ControlFlow.InjectionPoint {
		return ControlFlow.InjectionPoint.AfterAnalysis
	}

	override fun priority(): Int {
		return 100
	}
}