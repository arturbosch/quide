package io.gitlab.arturbosch.quide.detekt

import io.gitlab.arturbosch.detekt.api.YamlConfig
import io.gitlab.arturbosch.detekt.core.Detekt
import io.gitlab.arturbosch.detekt.core.PathFilter
import io.gitlab.arturbosch.quide.detection.Detector
import io.gitlab.arturbosch.quide.platform.UserData
import java.nio.file.Paths

/**
 * @author Artur Bosch
 */
class DetektTool : Detector<DetektCodeSmell> {

	override fun name(): String {
		return "Detekt"
	}

	override fun <U : UserData> execute(data: U) {
		data.projectPath().ifPresent {
			val config = YamlConfig.load(
					Paths.get("/home/artur/Repos/detekt/default-detekt-config.yml"))
			val filters = listOf(".*test.*").map(::PathFilter)
			val detektion = Detekt(it, config, pathFilters = filters).run()
			val smells = DetektSmellContainer(detektion.findings
					.flatMap { it.value }
					.map(::DetektCodeSmell))
			data.put("currentContainer", smells)
		}
	}

}
