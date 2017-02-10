package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.quide.platform.ControlFlow
import io.gitlab.arturbosch.quide.platform.Processor
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.smartsmells.api.DetectorFacade
import io.gitlab.arturbosch.smartsmells.api.UpdatableDetectorFacade
import io.gitlab.arturbosch.smartsmells.config.DetectorConfig
import java.nio.file.Files

/**
 * @author Artur Bosch
 */
class DetectorFacadeProcessor : Processor {

	override fun <U : UserData> execute(data: U) {
		val pluginData = data as JavaPluginData
		val configPath = data.quideDirectory().configurationsDir().resolve("smartsmells.yml")
		val facade = if (Files.exists(configPath)) {
			DetectorFacade.fromConfig(DetectorConfig.load(configPath))
		} else {
			DetectorFacade.fullStackFacade()
		}
		if (pluginData.isEvolutionaryAnalysis()) {
			data.put(UPDATABLE_FACADE, UpdatableDetectorFacade(facade))
		}
		data.put(FACADE, facade)
	}

	override fun injectionPoint(): ControlFlow.InjectionPoint {
		return ControlFlow.InjectionPoint.BeforeAnalysis
	}
}