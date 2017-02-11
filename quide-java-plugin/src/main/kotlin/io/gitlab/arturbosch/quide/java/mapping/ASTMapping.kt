package io.gitlab.arturbosch.quide.java.mapping

import io.gitlab.arturbosch.quide.java.JavaCodeSmell
import io.gitlab.arturbosch.quide.java.JavaSmellContainer
import io.gitlab.arturbosch.quide.mapping.SmellCompareStrategy
import io.gitlab.arturbosch.quide.mapping.SmellMapping
import io.gitlab.arturbosch.quide.model.SmellContainer
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.quide.vcs.DiffTool
import io.gitlab.arturbosch.quide.vcs.FileChange
import io.gitlab.arturbosch.quide.vcs.Versionable

/**
 * @author Artur Bosch
 */
class ASTMapping : SmellMapping<JavaCodeSmell> {

	private val compare = ASTCompareStrategy()
	private val diff = ASTDiffTool()

	override fun <U : UserData> execute(data: U) {
		val currentVersion = data.currentVersion()
		val currentContainer = data.currentContainer<JavaSmellContainer, JavaCodeSmell>()
		val lastContainer = data.lastContainer<JavaSmellContainer, JavaCodeSmell>()
		val lastVersion = data.lastVersion()
		val mappedContainer = if (!lastVersion.isPresent && currentVersion.isPresent) {
			mapFirstVersion(currentVersion.get(), currentContainer.get())
		} else {
			assert(currentContainer.isPresent)
			assert(lastContainer.isPresent)
			assert(lastVersion.isPresent)
			assert(currentVersion.isPresent)
			map(currentVersion.get(), lastContainer.get(), currentContainer.get())
		}
		data.put(UserData.CURRENT_CONTAINER, mappedContainer)
	}

	override fun compareAlgorithm(): SmellCompareStrategy<JavaCodeSmell> = compare

	override fun diffTool(): DiffTool<ASTPatch> = diff

	override fun map(versionable: Versionable,
					 before: SmellContainer<JavaCodeSmell>,
					 after: SmellContainer<JavaCodeSmell>): SmellContainer<JavaCodeSmell> {
		val changes = versionable.fileChanges()

		// all living smells get new end version before modifying them
		before.alive().forEach { it.setEndVersion(versionable) }
		// modified smells need to get mapped
		val modifications = changes.filter { it.isOfType(FileChange.Type.MODIFICATION) }
		modifications.forEach { fileChange ->
			val oldFile = fileChange.oldFile()
			val newFile = fileChange.newFile()
			val smellsInOldFile = before.findBySourcePath(oldFile.path())
			val smellsInNewFile = after.findBySourcePath(newFile.path())
			// Smell Equality? -> nothing to do as end version is already set
			// just add mapped smell in separate list to distinct from really new smells
			fun mapUnchangedSmells(beforeSmells: MutableList<JavaCodeSmell>,
								   afterSmells: MutableList<JavaCodeSmell>):
					Pair<MutableList<JavaCodeSmell>, MutableList<JavaCodeSmell>> {

				val unmappedSmells = mutableListOf<JavaCodeSmell>()
				val mappedSmells = mutableListOf <JavaCodeSmell>()
				beforeSmells.forEach { findSmell ->
					afterSmells.find { compareSmells(it, findSmell) }?.let {
						// when found, we need the new smell specific information like
						// new source path etc so we copy version information and work with the new smell
						it.copyVersionInformationFrom(findSmell)
						if (!it.isAlive) { // is resurrected smell?
							it.setEndVersion(versionable)
							it.revivedIn(versionable)
							it.addWeight(1) // extra weight
							it.isAlive = true
						}
						before.all().remove(findSmell)
						mappedSmells.add(it)
						before.addSmell(it)
					} ?: unmappedSmells.add(findSmell) // Not equal
					// but maybe still the same smell -> search for growth in AST
				}
				return unmappedSmells to mappedSmells
			}

			val (unmappedSmells, mappedSmells) = mapUnchangedSmells(smellsInOldFile, smellsInNewFile)
			// We try to search for modified smells with the help of an AST
			val notMappedSmellsInNewFile = smellsInNewFile.minus(mappedSmells).toMutableList()
			val patchedSmells = unmappedSmells.asSequence()
					.filter { it.isAlive } // do not need to try patch dead smells which may be got resurrected
					.map { updateSmell(it, fileChange) } // ^^ they were not in last version, so no diff can be created
					.toMutableList()
			val (removedSmells, astMappedSmells) = mapUnchangedSmells(patchedSmells, notMappedSmellsInNewFile)
			astMappedSmells.forEach { it.addWeight(1) } // modified smells get extra weight

			// Remaining not found smells in the AST were deleted
			removedSmells.forEach { it.killedIn(versionable) }
			// As they are truly new smells in modified file -> add to container
			val newSmells = notMappedSmellsInNewFile.minus(astMappedSmells).toMutableList()
			newSmells.forEach {
				it.applyVersion(versionable)
				before.addSmell(it)
			}
		}

		// deleted smells are set to alive=false
		handleDeleted(versionable, before, changes)
		before.all().addAll(handleAdditions(versionable, after, changes))

		return before
	}

	private fun handleDeleted(versionable: Versionable,
							  before: SmellContainer<JavaCodeSmell>,
							  changes: MutableList<FileChange>): List<JavaCodeSmell> {
		val deletedPaths = changes.filter { it.isOfType(FileChange.Type.REMOVAL) }
				.map { it.oldFile() }
				.map { it.path() }
		val deletedSmells = deletedPaths.map { before.findBySourcePath(it) }
				.flatMap { it }
		deletedSmells.forEach { it.killedIn(versionable) }
		return deletedSmells
	}

	private fun handleAdditions(versionable: Versionable,
								after: SmellContainer<JavaCodeSmell>,
								changes: MutableList<FileChange>): List<JavaCodeSmell> {
		val newPaths = changes.filter { it.isOfType(FileChange.Type.ADDITION) }
				.map { it.newFile() }
				.map { it.path() }
		val newSmells = newPaths.map { after.findBySourcePath(it) }
				.flatMap { it }
		newSmells.forEach { it.applyVersion(versionable) }
		return newSmells
	}
}