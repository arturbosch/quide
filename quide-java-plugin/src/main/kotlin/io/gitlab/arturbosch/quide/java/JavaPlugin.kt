package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.quide.detection.Detector
import io.gitlab.arturbosch.quide.platform.Plugin
import io.gitlab.arturbosch.quide.platform.Processor
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.quide.platform.processors.NumberOfSmellsProcessor
import io.gitlab.arturbosch.quide.platform.processors.ResultPrintProcessor

/**
 * @author Artur Bosch
 */
class JavaPlugin : Plugin {
	override fun detector(): Detector<JavaSmellContainer> {
		return SmartSmellsTool()
	}

	override fun processors(): MutableList<Processor> {
		return mutableListOf(NumberOfSmellsProcessor(), ResultPrintProcessor())
	}

	override fun userData(): UserData {
		return object : UserData() {}
	}
}