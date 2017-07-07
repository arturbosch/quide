package io.gitlab.arturbosch.quide.java.core

import io.gitlab.arturbosch.kutils.exists
import io.gitlab.arturbosch.kutils.isFile
import io.gitlab.arturbosch.quide.java.FACADE
import io.gitlab.arturbosch.quide.java.JavaPluginData
import io.gitlab.arturbosch.quide.java.PATHS_FILTERS_JAVA
import io.gitlab.arturbosch.quide.java.PLUGIN_JAVA_CONFIG
import io.gitlab.arturbosch.quide.java.UPDATABLE_FACADE
import io.gitlab.arturbosch.quide.platform.ControlFlow
import io.gitlab.arturbosch.quide.platform.Processor
import io.gitlab.arturbosch.quide.platform.QuideConstants
import io.gitlab.arturbosch.quide.platform.QuideDirectory
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.smartsmells.api.DetectorFacade
import io.gitlab.arturbosch.smartsmells.api.DetectorLoader
import io.gitlab.arturbosch.smartsmells.api.JarLoader
import io.gitlab.arturbosch.smartsmells.api.UpdatableDetectorFacade
import io.gitlab.arturbosch.smartsmells.config.DetectorConfig
import io.gitlab.arturbosch.smartsmells.config.dsl.DetectorConfigDslRunner
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Artur Bosch
 */
class DetectorFacadeProcessor : Processor {

	override fun <U : UserData> execute(data: U) {
		val pluginData = data as JavaPluginData
		val quide = data.quideDirectory()
		val config = quide.getPropertyOrDefault(PLUGIN_JAVA_CONFIG, "")
		val configPath = quide.configurationsDir().resolve(config)
		val filters = loadFiltersFromProperties(quide)
		val facade = buildFacade(configPath, filters)
		if (pluginData.isEvolutionaryAnalysis) {
			data.put(UPDATABLE_FACADE, UpdatableDetectorFacade(facade))
		}
		data.put(FACADE, facade)
	}

	private fun buildFacade(configPath: Path, filters: List<String>): DetectorFacade {
		if (configPath.exists() && configPath.isFile()) {
			val configPathAsString = configPath.toString()
			if (configPathAsString.endsWith(".yml")) {
				return DetectorFacade.builder()
						.withFilters(filters)
						.fromConfig(DetectorConfig.load(configPath))
						.build()
			} else if (configPathAsString.endsWith(".groovy")) {
				val dsl = DetectorConfigDslRunner.execute(configPath.toFile())
				val jars = dsl.jars.map { Paths.get(it) }
				return DetectorFacade.builder()
						.withFilters(filters)
						.fromConfig(dsl.build())
						.withLoader(DetectorLoader(JarLoader(jars)))
						.build()
			}
		}
		ControlFlow.LOGGER.info("No configuration found, using the full stack facade...")
		return DetectorFacade.builder().withFilters(filters).fullStackFacade()
	}

	private fun loadFiltersFromProperties(quideDirectory: QuideDirectory): List<String> {
		val globalFilters = quideDirectory.getPropertyOrDefault(QuideConstants.PATHS_FILTERS_GLOBAL, "").trim()
		val javaFilters = quideDirectory.getPropertyOrDefault(PATHS_FILTERS_JAVA, "").trim()
		return globalFilters.split(',').plus(javaFilters.split(',')).filterNot(String::isNullOrBlank)
	}

	override fun injectionPoint(): ControlFlow.InjectionPoint {
		return io.gitlab.arturbosch.quide.platform.ControlFlow.InjectionPoint.BeforeAnalysis
	}
}
