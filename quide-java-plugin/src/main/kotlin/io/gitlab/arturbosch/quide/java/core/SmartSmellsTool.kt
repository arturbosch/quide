package io.gitlab.arturbosch.quide.java.core

import io.gitlab.arturbosch.quide.detection.Detector
import io.gitlab.arturbosch.quide.java.JavaPluginData
import io.gitlab.arturbosch.quide.platform.UserData

/**
 * @author Artur Bosch
 */
class SmartSmellsTool : Detector<JavaSmellContainer> {

	override fun name(): String {
		return "SmartSmells"
	}

	override fun <U : UserData> execute(data: U): JavaSmellContainer {
		val pluginData = data as JavaPluginData
		val currentVersion = data.currentVersion()
		val projectPath = pluginData.projectPath()

		val smellResult = if (pluginData.isEvolutionaryAnalysis()) {
			val updatableFacade = pluginData.updatableFacade()
			val versionable = currentVersion.orElseThrow { IllegalStateException("In evolutionary analysis but no version?") }
			VersionCrawler(projectPath, versionable, updatableFacade).run()
		} else {
			pluginData.facade().run(projectPath)
		}

		return JavaSmellContainer(smellResult)
	}

}
