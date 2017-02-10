package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.quide.detection.Detector
import io.gitlab.arturbosch.quide.platform.Plugin
import io.gitlab.arturbosch.quide.platform.Processor
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.quide.platform.processors.NumberOfSmellsProcessor

/**
 * @author Artur Bosch
 */
class JavaPlugin : Plugin {

	private val storage = JavaPluginData()

	override fun detector(): Detector<JavaSmellContainer> {
		return SmartSmellsTool()
	}

	override fun processors(): MutableList<Processor> {
		return mutableListOf(DetectorFacadeProcessor(), MappingProcessor(), NumberOfSmellsProcessor())
	}

	override fun userData(): UserData {
		return storage
	}
}