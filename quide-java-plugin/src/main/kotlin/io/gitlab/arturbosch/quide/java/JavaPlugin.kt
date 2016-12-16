package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.quide.detection.Detector
import io.gitlab.arturbosch.quide.platform.Plugin
import io.gitlab.arturbosch.quide.platform.Processor
import io.gitlab.arturbosch.quide.platform.UserData

/**
 * @author Artur Bosch
 */
class JavaPlugin : Plugin {
	override fun detector(): Detector<*> {
		return SmartSmellsTool()
	}

	override fun processors(): MutableList<Processor> {
		return mutableListOf(PrintProcessor(), SmartSmellsMapping())
	}

	override fun userData(): UserData {
		return object : UserData() {}
	}
}