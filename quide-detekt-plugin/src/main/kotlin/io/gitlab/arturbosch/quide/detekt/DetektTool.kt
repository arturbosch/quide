package io.gitlab.arturbosch.quide.detekt

import io.gitlab.arturbosch.detekt.api.YamlConfig
import io.gitlab.arturbosch.detekt.core.Detekt
import io.gitlab.arturbosch.detekt.core.PathFilter
import io.gitlab.arturbosch.detekt.core.ProcessingSettings
import io.gitlab.arturbosch.quide.detection.Detector
import io.gitlab.arturbosch.quide.platform.QuideConstants
import io.gitlab.arturbosch.quide.platform.QuideDirectory
import io.gitlab.arturbosch.quide.platform.UserData

/**
 * @author Artur Bosch
 */
class DetektTool : Detector<DetektSmellContainer> {

	override fun name(): String = "Detekt"

	override fun <U : UserData> execute(data: U): DetektSmellContainer {
		val projectPath = data.projectPath()
		val quide = data.quideDirectory()
		val configSuffix = quide.getProperty(PLUGIN_KOTLIN_CONFIG) ?:
				throw IllegalStateException("A property $PLUGIN_KOTLIN_CONFIG must be provided to load detekt configurations!")
		val configPath = quide.configurationsDir().resolve(configSuffix)
		val config = YamlConfig.load(configPath)
		val filters = loadFiltersFromProperties(quide).map(::PathFilter)
		val detektion = Detekt(projectPath, config, emptyList(),
				ProcessingSettings(filters, true, emptyList())
		).run()
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
