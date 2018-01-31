package io.gitlab.arturbosch.quide.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.YamlConfig
import io.gitlab.arturbosch.detekt.cli.out.XmlOutputReport
import io.gitlab.arturbosch.detekt.core.DetektFacade
import io.gitlab.arturbosch.detekt.core.PathFilter
import io.gitlab.arturbosch.detekt.core.ProcessingSettings
import io.gitlab.arturbosch.quide.api.AnalysisContext
import io.gitlab.arturbosch.quide.api.Detector
import io.gitlab.arturbosch.quide.api.core.PATHS_FILTERS_GLOBAL
import io.gitlab.arturbosch.quide.api.core.QuideDir
import mu.KLogging

/**
 * @author Artur Bosch
 */
class DetektTool : Detector {

	companion object : KLogging()

	override val name: String = "Detekt"

	override fun execute(context: AnalysisContext) {
		val fs = context.fileSystem
		val quide = context.quideDirectory
		val configPath = context.property(PLUGIN_KOTLIN_CONFIG)
		val config = configPath?.let {
			val configFile = quide.configurationsDir.resolve(configPath)
			YamlConfig.load(configFile.toPath())
		} ?: Config.empty.apply {
			logger.warn("The detekt configuration property '$PLUGIN_KOTLIN_CONFIG' was not provided!")
		}

		val filters = loadFiltersFromProperties(quide).map(::PathFilter)
		val settings = ProcessingSettings(fs.projectDir.toPath(), config, filters,
				true, false, emptyList())
		val detektion = DetektFacade.create(settings).run()
		val report = XmlOutputReport()
		val result = report.render(detektion)
		fs.workDir.resolve(report.fileName + "." + report.ending).writeText(result)
	}

	private fun loadFiltersFromProperties(quideDirectory: QuideDir): List<String> {
		val globalFilters = quideDirectory.propertyOrDefault(PATHS_FILTERS_GLOBAL, "").trim()
		val kotlinFilters = quideDirectory.propertyOrDefault(PATHS_FILTERS_KOTLIN, "").trim()
		return globalFilters.split(',').plus(kotlinFilters.split(',')).filterNot(String::isNullOrBlank)
	}
}
