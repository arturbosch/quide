package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.quide.detection.Detector
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.quide.vcs.FileChange
import io.gitlab.arturbosch.quide.vcs.QuideGitVersionProvider
import io.gitlab.arturbosch.smartsmells.api.DetectorFacade
import io.gitlab.arturbosch.smartsmells.api.SmellResult
import io.gitlab.arturbosch.smartsmells.api.UpdatableDetectorFacade
import io.gitlab.arturbosch.smartsmells.config.DetectorConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
class SmartSmellsTool : Detector<JavaSmellContainer> {

	val logger: Logger = LoggerFactory.getLogger(javaClass.simpleName)

	override fun name(): String {
		return "SmartSmells"
	}

	override fun <U : UserData> execute(data: U): JavaSmellContainer {
		val projectPath = data.projectPath()
		val configPath = data.quideDirectory().configurationsDir().resolve("smartsmells.yml")
		val facade = DetectorFacade.fromConfig(DetectorConfig.load(configPath))
		val updatableDetectorFacade = UpdatableDetectorFacade(projectPath, facade)

		val smellResult = loopVersions(projectPath, updatableDetectorFacade)!!
		return JavaSmellContainer(smellResult)
	}

	private fun loopVersions(projectPath: Path, facade: UpdatableDetectorFacade): SmellResult? {
		val versionProvider = QuideGitVersionProvider()
		var nextVersion = versionProvider.nextVersion()
		var result: SmellResult? = null
		while (nextVersion.isPresent) {
			val current = nextVersion.get()
			logger.info("${current.versionNumber()} - ${current.revision().message()}")
			val groupBy = current.fileChanges().filter { it.isOfType(FileChange.Type.ADDITION) }
					.map { it.newFile() }.map { versionProvider.root.resolve(it.path()) to it.content() }
					.filter { it.first.toString().endsWith(".java") }
					.filter { !it.second.isNullOrEmpty() }
					.toMap()
			val infos = facade.addOrUpdate(groupBy)
			result = facade.run(infos)
			nextVersion = versionProvider.nextVersion()
		}
		return result
	}

}
