package io.gitlab.arturbosch.quide.java.core

import com.github.javaparser.utils.Pair
import io.gitlab.arturbosch.quide.java.JAVA
import io.gitlab.arturbosch.quide.vcs.FileChange
import io.gitlab.arturbosch.quide.vcs.Versionable
import io.gitlab.arturbosch.smartsmells.api.SmellResult
import io.gitlab.arturbosch.smartsmells.api.UpdatableDetectorFacade
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
class VersionCrawler(private val projectPath: Path,
					 private val versionable: Versionable,
					 private val facade: UpdatableDetectorFacade) {

	private val logger: Logger = LoggerFactory.getLogger(javaClass.simpleName)

	fun run(): SmellResult = generateNewCompilationUnits().apply {
		logger.info("Successful executed detectors with ${smellSets.map { "${it.key} - ${it.value.size} " }}")
	}

	private fun generateNewCompilationUnits(): SmellResult {

		val sequence = versionable.fileChanges().asSequence()
		val additions = sequence.filter { it.isOfType(FileChange.Type.ADDITION) }
				.map { it.newFile() }.map { projectPath.resolve(it.path()) to it.content() }
				.filter { it.first.toString().endsWith(JAVA) }
				.filter { !it.second.isNullOrEmpty() }
				.toMap()
		val modifications = sequence.filter { it.isOfType(FileChange.Type.MODIFICATION) }
				.map { it.newFile() }.map { projectPath.resolve(it.path()) to it.content() }
				.filter { it.first.toString().endsWith(JAVA) }
				.filter { !it.second.isNullOrEmpty() }
				.toMap()
		val relocations = sequence.filter { it.isOfType(FileChange.Type.RELOCATION) }
				.map { change ->
					projectPath.resolve(change.oldFile().path()) to
							Pair(projectPath.resolve(change.newFile().path()), change.newFile().content())
				}
				.filter { it.first.toString().endsWith(JAVA) }
				.filter { it.second.a.toString().endsWith(JAVA) }
				.filter { !it.second.b.isNullOrEmpty() }
				.toMap()
		val deletions = sequence.filter { it.isOfType(FileChange.Type.REMOVAL) }
				.map { it.oldFile() }
				.map { projectPath.resolve(it.path()) }
				.filter { it.toString().endsWith(JAVA) }
				.toList()
		val size = additions.size + modifications.size + relocations.size
		logger.info("Running detectors with $size compilation info's.")
		facade.remove(deletions)
		facade.addOrUpdate(additions)
		facade.addOrUpdate(modifications)
		facade.relocateWithContent(relocations)
		return facade.run()
	}

}
