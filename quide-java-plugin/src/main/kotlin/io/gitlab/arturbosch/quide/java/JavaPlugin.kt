package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.quide.detection.Detector
import io.gitlab.arturbosch.quide.java.core.ContainerToXmlProcessor
import io.gitlab.arturbosch.quide.java.core.DetectorFacadeProcessor
import io.gitlab.arturbosch.quide.java.core.JavaSmellContainer
import io.gitlab.arturbosch.quide.java.core.MappingProcessor
import io.gitlab.arturbosch.quide.java.core.ResultXmlProcessor
import io.gitlab.arturbosch.quide.java.core.SmartSmellsTool
import io.gitlab.arturbosch.quide.platform.Plugin
import io.gitlab.arturbosch.quide.platform.Processor
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.quide.platform.processors.NumberOfSmellsProcessor
import io.gitlab.arturbosch.quide.platform.processors.ResultPrintProcessor
import io.gitlab.arturbosch.quide.platform.reflect.TypeToken
import io.gitlab.arturbosch.smartsmells.api.DetectorFacade
import io.gitlab.arturbosch.smartsmells.api.UpdatableDetectorFacade

/**
 * @author Artur Bosch
 */
class JavaPlugin : Plugin {

	private val storage = JavaPluginData()

	override fun detector(): Detector<JavaSmellContainer> {
		return SmartSmellsTool()
	}

	override fun processors(): MutableList<Processor> {
		return mutableListOf(DetectorFacadeProcessor(), MappingProcessor(), NumberOfSmellsProcessor(),
				ContainerToXmlProcessor(), ResultXmlProcessor(), ResultPrintProcessor())
	}

	override fun userData(): UserData {
		return storage
	}
}

class JavaPluginData : UserData() {

	fun isEvolutionaryAnalysis(): Boolean {
		return versionProvider().isPresent
	}

	fun updatableFacade(): UpdatableDetectorFacade = get(UPDATABLE_FACADE,
			TypeToken.get(UpdatableDetectorFacade::class.java)).orElseThrow {
		IllegalStateException("Only use this method if you are sure quide is running in an evolutionary analysis!")
	}

	fun facade(): DetectorFacade = get(FACADE,
			TypeToken.get(DetectorFacade::class.java)).orElseThrow {
		IllegalStateException("There was no detector facade inside user data!")
	}

}

const val FACADE: String = "FACADE"
const val UPDATABLE_FACADE: String = "UPDATABLE_FACADE"