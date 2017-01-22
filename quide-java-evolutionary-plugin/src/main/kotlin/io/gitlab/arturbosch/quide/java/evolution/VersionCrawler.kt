package io.gitlab.arturbosch.quide.java.evolution

import com.github.javaparser.utils.Pair
import io.gitlab.arturbosch.quide.vcs.FileChange
import io.gitlab.arturbosch.quide.vcs.QuideGitVersionProvider
import io.gitlab.arturbosch.quide.vcs.Versionable
import io.gitlab.arturbosch.smartsmells.api.SmellResult
import io.gitlab.arturbosch.smartsmells.api.UpdatableDetectorFacade
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
class VersionCrawler(projectPath: Path, private val facade: UpdatableDetectorFacade) {

	private val logger: Logger = LoggerFactory.getLogger(javaClass.simpleName)
	private val versionProvider = QuideGitVersionProvider(projectPath)

	fun loop(): SmellResult? {
		var nextVersion = versionProvider.nextVersion()
		var result: SmellResult? = null
		while (nextVersion.isPresent) {
			val current = nextVersion.get()
			logger.info("${current.versionNumber()} - ${current.revision().date()} - ${current.revision().message()}")
			result = generateNewCompilationUnits(current)
			logger.info("Successful executed detectors with ${result.smellSets.map { "${it.key} - ${it.value.size} " }}")
			nextVersion = versionProvider.nextVersion()
		}
		return result
	}

	private fun generateNewCompilationUnits(current: Versionable): SmellResult {

		val sequence = current.fileChanges().asSequence()
		val additions = sequence.filter { it.isOfType(FileChange.Type.ADDITION) }
				.map { it.newFile() }.map { versionProvider.root!!.resolve(it.path()) to it.content() }
				.filter { it.first.toString().endsWith(".java") }
				.filter { !it.second.isNullOrEmpty() }
				.toMap()
		val modifications = sequence.filter { it.isOfType(FileChange.Type.MODIFICATION) }
				.map { it.newFile() }.map { versionProvider.root!!.resolve(it.path()) to it.content() }
				.filter { it.first.toString().endsWith(".java") }
				.filter { !it.second.isNullOrEmpty() }
				.toMap()
		val relocations = sequence.filter { it.isOfType(FileChange.Type.RELOCATION) }
				.map { change ->
					versionProvider.root!!.resolve(change.oldFile().path()) to
							Pair(versionProvider.root!!.resolve(change.newFile().path()), change.newFile().content())
				}
				.filter { it.first.toString().endsWith(".java") }
				.filter { it.second.a.toString().endsWith(".java") }
				.filter { !it.second.b.isNullOrEmpty() }
				.toMap()
		val deletions = sequence.filter { it.isOfType(FileChange.Type.REMOVAL) }
				.map { it.oldFile() }
				.map { versionProvider.root!!.resolve(it.path()) }
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