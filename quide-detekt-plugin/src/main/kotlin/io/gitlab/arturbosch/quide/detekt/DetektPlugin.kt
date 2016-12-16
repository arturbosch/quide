package io.gitlab.arturbosch.quide.detekt

import io.gitlab.arturbosch.quide.detection.Detector
import io.gitlab.arturbosch.quide.platform.Plugin
import io.gitlab.arturbosch.quide.platform.Processor
import io.gitlab.arturbosch.quide.platform.UserData

/**
 * @author Artur Bosch
 */
class DetektPlugin : Plugin {
	override fun detector(): Detector<*> {
		return DetektTool()
	}

	override fun processors(): MutableList<Processor> {
		return mutableListOf(PrintProcessor(), DetektMapping())
	}

	override fun userData(): UserData {
		return object : UserData() {}
	}

}