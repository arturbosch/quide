package io.gitlab.arturbosch.quide.hotspot

import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.quide.platform.processors.EvolutionaryProcessor
import java.nio.file.Paths

/**
 * @author Artur Bosch
 */
class HotspotReporter : EvolutionaryProcessor {

	override fun <U : UserData> doIfActive(data: U) {
		val projectPath = data.projectPath()
		val paths = (data as HotspotData).paths
		paths.forEach { path, activity ->
			val relative = projectPath.relativize(Paths.get(path))
			println("$relative - $activity")
		}
	}
}