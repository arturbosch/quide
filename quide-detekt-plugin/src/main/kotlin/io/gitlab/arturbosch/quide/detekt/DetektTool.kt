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
class DetektTool : Detector<DetektSmellContainer> {

	override fun name(): String {
		return "Detekt"
	}

	override fun <U : UserData> execute(data: U): DetektSmellContainer {
		val projectPath = data.projectPath()
		val config = YamlConfig.load(
				Paths.get("/home/artur/Repos/detekt/default-detekt-config.yml"))
		val filters = listOf(".*test.*").map(::PathFilter)
		val detektion = Detekt(projectPath, config, pathFilters = filters).run()
		return DetektSmellContainer(detektion.findings
				.flatMap { it.value }
				.map(::DetektCodeSmell)).apply {
			data.put("currentContainer", this)
		}

	}

}
