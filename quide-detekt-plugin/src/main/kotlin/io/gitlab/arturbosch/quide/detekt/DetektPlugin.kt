package io.gitlab.arturbosch.quide.detekt

import io.gitlab.arturbosch.quide.detection.CodeSmellDetector
import io.gitlab.arturbosch.quide.platform.Plugin
import io.gitlab.arturbosch.quide.platform.Processor
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.quide.platform.processors.NumberOfSmellsProcessor
import io.gitlab.arturbosch.quide.platform.processors.ResultPrintProcessor

/**
 * @author Artur Bosch
 */
class DetektPlugin : Plugin {

	private val storage = object : UserData() {}
	private val tool = DetektTool()
	private val processors = mutableListOf(NumberOfSmellsProcessor(), ResultPrintProcessor())

	override fun name(): String = "KotlinPlugin"

	override fun detector(): CodeSmellDetector<DetektSmellContainer> = tool

	override fun processors(): MutableList<Processor> = processors

	override fun userData(): UserData = storage

}