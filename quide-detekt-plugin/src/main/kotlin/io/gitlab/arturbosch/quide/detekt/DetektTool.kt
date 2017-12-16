package io.gitlab.arturbosch.quide.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.YamlConfig
import io.gitlab.arturbosch.detekt.core.DetektFacade
import io.gitlab.arturbosch.detekt.core.PathFilter
import io.gitlab.arturbosch.detekt.core.ProcessingSettings
import io.gitlab.arturbosch.quide.detection.CodeSmellDetector
import io.gitlab.arturbosch.quide.platform.ControlFlow
import io.gitlab.arturbosch.quide.platform.QuideConstants
import io.gitlab.arturbosch.quide.platform.QuideDirectory
import io.gitlab.arturbosch.quide.platform.UserData

/**
 * @author Artur Bosch
 */
class DetektTool : CodeSmellDetector<DetektSmellContainer> {

	override fun name(): String = "Detekt"

	override fun <U : UserData> execute(data: U): DetektSmellContainer {
		val projectPath = data.projectPath()
		val quide = data.quideDirectory()
		val configSuffix = quide.getProperty(PLUGIN_KOTLIN_CONFIG)

		val config = configSuffix?.let {
			val configPath = quide.configurationsDir().resolve(configSuffix)
			YamlConfig.load(configPath)
		} ?: Config.empty.apply {
			ControlFlow.LOGGER.warn("The detekt configuration property '$PLUGIN_KOTLIN_CONFIG' was not provided!")
		}

		val filters = loadFiltersFromProperties(quide).map(::PathFilter)
		val settings = ProcessingSettings(projectPath, config, filters, true, false, emptyList())
		val detektion = DetektFacade.instance(settings).run()
		return DetektSmellContainer(detektion.findings
				.flatMap { it.value }
				.map(::DetektCodeSmell))
	}

	private fun loadFiltersFromProperties(quideDirectory: QuideDirectory): List<String> {
		val globalFilters = quideDirectory.getPropertyOrDefault(QuideConstants.PATHS_FILTERS_GLOBAL, "").trim()
		val javaFilters = quideDirectory.getPropertyOrDefault(PATHS_FILTERS_KOTLIN, "").trim()
		return globalFilters.split(',').plus(javaFilters.split(',')).filterNot(String::isNullOrBlank)
	}
}
