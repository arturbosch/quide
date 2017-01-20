package io.gitlab.arturbosch.quide.java

import com.github.javaparser.utils.Pair
import io.gitlab.arturbosch.quide.detection.Detector
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.quide.vcs.FileChange
import io.gitlab.arturbosch.quide.vcs.QuideGitVersionProvider
import io.gitlab.arturbosch.quide.vcs.Versionable
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
		val versionProvider = QuideGitVersionProvider(projectPath)
		var nextVersion = versionProvider.nextVersion()
		var result: SmellResult? = null
		while (nextVersion.isPresent) {
			val current = nextVersion.get()
			logger.info("${current.versionNumber()} - ${current.revision().date()} - ${current.revision().message()}")
			result = generateNewCompilationUnits(versionProvider, facade, current)
			logger.info("Successful executed detectors with ${result.smellSets.map { "${it.key} - ${it.value.size} " }}")
			nextVersion = versionProvider.nextVersion()
		}
		return result
	}

	private fun generateNewCompilationUnits(versionProvider: QuideGitVersionProvider,
											facade: UpdatableDetectorFacade,
											current: Versionable): SmellResult {
		val sequence = current.fileChanges().asSequence()
		val additions = sequence.filter { it.isOfType(FileChange.Type.ADDITION) }
				.map { it.newFile() }.map { versionProvider.root.resolve(it.path()) to it.content() }
				.filter { it.first.toString().endsWith(".java") }
				.filter { !it.second.isNullOrEmpty() }
				.toMap()
		val modifications = sequence.filter { it.isOfType(FileChange.Type.MODIFICATION) }
				.map { it.newFile() }.map { versionProvider.root.resolve(it.path()) to it.content() }
				.filter { it.first.toString().endsWith(".java") }
				.filter { !it.second.isNullOrEmpty() }
				.toMap()
		val relocations = sequence.filter { it.isOfType(FileChange.Type.RELOCATION) }
				.map { change -> versionProvider.root.resolve(change.oldFile().path()) to
						Pair(versionProvider.root.resolve(change.newFile().path()), change.newFile().content()) }
				.filter { it.first.toString().endsWith(".java") }
				.filter { it.second.a.toString().endsWith(".java") }
				.filter { !it.second.b.isNullOrEmpty() }
				.toMap()
		val deletions = sequence.filter { it.isOfType(FileChange.Type.REMOVAL) }
				.map { it.oldFile() }
				.map { versionProvider.root.resolve(it.path()) }
				.filter { it.toString().endsWith(".java") }
				.toList()
		logger.info("Parsed all changes...")
		val size = additions.size + modifications.size + relocations.size
		logger.info("Running detectors with $size compilation info's.")
		facade.remove(deletions)
		facade.addOrUpdate(additions)
		facade.addOrUpdate(modifications)
		facade.relocateWithContent(relocations)
		return facade.run()
	}
}
