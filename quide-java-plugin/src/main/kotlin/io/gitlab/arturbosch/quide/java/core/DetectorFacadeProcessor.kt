package io.gitlab.arturbosch.quide.java.core

import com.github.javaparser.ParserConfiguration
import io.gitlab.arturbosch.jpal.core.JPAL
import io.gitlab.arturbosch.jpal.core.JavaCompilationParser
import io.gitlab.arturbosch.jpal.core.UpdatableCompilationStorage
import io.gitlab.arturbosch.kutils.exists
import io.gitlab.arturbosch.kutils.isFile
import io.gitlab.arturbosch.quide.java.FACADE
import io.gitlab.arturbosch.quide.java.JavaPluginData
import io.gitlab.arturbosch.quide.java.PLUGIN_JAVA_CONFIG
import io.gitlab.arturbosch.quide.java.UPDATABLE_FACADE
import io.gitlab.arturbosch.quide.java.UPDATABLE_STORAGE
import io.gitlab.arturbosch.quide.java.loadFiltersFromProperties
import io.gitlab.arturbosch.quide.platform.ControlFlow
import io.gitlab.arturbosch.quide.platform.Processor
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.quide.platform.reflect.TypeToken
import io.gitlab.arturbosch.smartsmells.api.DetectorFacade
import io.gitlab.arturbosch.smartsmells.api.DetectorLoader
import io.gitlab.arturbosch.smartsmells.api.JarLoader
import io.gitlab.arturbosch.smartsmells.api.UpdatableDetectorFacade
import io.gitlab.arturbosch.smartsmells.config.DetectorConfig
import io.gitlab.arturbosch.smartsmells.config.dsl.DetectorConfigDslRunner
import io.gitlab.arturbosch.smartsmells.metrics.FileMetricProcessor
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.ExecutorService
import java.util.regex.Pattern

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
		val workerPool = data.retrieveCommonExecutor()
		val facade = buildFacade(configPath, filters, workerPool)
		if (pluginData.isEvolutionaryAnalysis) {
			val storage = JPAL.builder()
					.updatable()
					.withFilters(filters.map { Pattern.compile(it) })
					.withParser(createJavaParser())
					.withProcessor(FileMetricProcessor())
					.withExecutor(workerPool)
					.build() as UpdatableCompilationStorage
			data.put(UPDATABLE_STORAGE, storage)
			data.put(UPDATABLE_FACADE, UpdatableDetectorFacade(facade, storage))
		}
		data.put(FACADE, facade)
	}

	private fun UserData.retrieveCommonExecutor() =
			get("platform.worker.pool", TypeToken.get(ExecutorService::class.java))
					.orElseThrow { IllegalStateException("ExecutorService from platform.executor key not found.") }

	private fun createJavaParser() = JavaCompilationParser(ParserConfiguration()
			.setStoreTokens(true)
			.setAttributeComments(false))

	private fun buildFacade(configPath: Path, filters: List<String>, workerPool: ExecutorService): DetectorFacade {
		if (configPath.exists() && configPath.isFile()) {
			val configPathAsString = configPath.toString()
			if (configPathAsString.endsWith(".yml")) {
				return DetectorFacade.builder()
						.withFilters(filters)
						.withExecutor(workerPool)
						.fromConfig(DetectorConfig.load(configPath))
						.build()
			} else if (configPathAsString.endsWith(".groovy")) {
				val dsl = DetectorConfigDslRunner.execute(configPath.toFile())
				val jars = dsl.jars.map { Paths.get(it) }
				return DetectorFacade.builder()
						.withFilters(filters)
						.fromConfig(dsl.build())
						.withLoader(DetectorLoader(JarLoader(jars)))
						.withExecutor(workerPool)
						.build()
			}
		}
		ControlFlow.LOGGER.info("No configuration found, using a full stack facade")
		return DetectorFacade.builder().withFilters(filters).fullStackFacade()
	}

	override fun injectionPoint(): ControlFlow.InjectionPoint {
		return io.gitlab.arturbosch.quide.platform.ControlFlow.InjectionPoint.BeforeAnalysis
	}
}
