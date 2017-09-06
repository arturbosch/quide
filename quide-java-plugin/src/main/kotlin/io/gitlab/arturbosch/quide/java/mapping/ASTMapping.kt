package io.gitlab.arturbosch.quide.java.mapping

import io.gitlab.arturbosch.quide.java.core.JavaCodeSmell
import io.gitlab.arturbosch.quide.java.core.JavaSmellContainer
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
	override fun compareAlgorithm(): SmellCompareStrategy<JavaCodeSmell> = compare
	override fun diffTool(): DiffTool<JavaCodeSmellPatch> = diff

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

	override fun map(versionable: Versionable,
					 before: SmellContainer<JavaCodeSmell>,
					 after: SmellContainer<JavaCodeSmell>): SmellContainer<JavaCodeSmell> {

		val changes = versionable.fileChanges().groupBy { it.type() }
		// all living smells get new end version before modifying them
		before.alive().forEach { it.setEndVersion(versionable) }
		// modified smells need to get mapped
		val mapper = ModificationMapper(this, after, before, versionable)
		changes[FileChange.Type.MODIFICATION]?.let { mapper.mapSmells(it) }
		changes[FileChange.Type.RELOCATION]?.let { mapper.mapSmells(it) }
		// deleted smells are set to alive=false
		changes[FileChange.Type.REMOVAL]?.let { handleDeleted(versionable, before, it) }
		changes[FileChange.Type.REMOVAL]?.let { handleAdditions(versionable, before, after, it) }

		return before
	}

	private class ModificationMapper(private val astMapping: ASTMapping,
									 private val after: SmellContainer<JavaCodeSmell>,
									 private val before: SmellContainer<JavaCodeSmell>,
									 private val versionable: Versionable) {

		internal fun mapSmells(modifications: List<FileChange>) {
			fun JavaCodeSmell.resurrectSmell() {
				setEndVersion(versionable)
				revivedIn(versionable)
				addWeight(1) // extra weight
				isAlive = true
			}

			modifications.forEach { fileChange ->
				val oldFile = fileChange.oldFile()
				val newFile = fileChange.newFile()
				val smellsInOldFile = before.findBySourcePath(oldFile.path())
				val smellsInNewFile = after.findBySourcePath(newFile.path())
				// Smell Equality? -> nothing to do as end version is already set
				// just add mapped smell in separate list to distinct from really new smells
				fun mapUnchangedSmells(beforeSmells: List<JavaCodeSmell>,
									   afterSmells: List<JavaCodeSmell>,
									   compareMethod: (JavaCodeSmell, JavaCodeSmell) -> Boolean =
									   { it, findSmell -> astMapping.compareSmells(it, findSmell) }):
						Pair<List<JavaCodeSmell>, List<JavaCodeSmell>> {

					val unmappedSmells = mutableListOf<JavaCodeSmell>()
					val mappedSmells = mutableListOf<JavaCodeSmell>()
					beforeSmells.forEach { findSmell ->
						afterSmells.find { compareMethod(it, findSmell) }?.let {
							// when found, we need the new smell specific information like
							// new source path etc so we copy version information and work with the new smell
							it.copyVersionInformationFrom(findSmell)
							if (!it.isAlive) it.resurrectSmell() // is resurrected smell?
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
				val notMappedSmellsInNewFile = smellsInNewFile.minus(mappedSmells)
				val patchedSmells = unmappedSmells
						.filter { it.isAlive } // do not need to try patch dead smells which may be got resurrected
						.map { astMapping.updateSmell(it, fileChange) } // ^^ they were not in last version, so no diff can be created
				// un-patched to patched to preserve state if patched is not mappable
				// Smells marked as dirty changed to much to be mappable/tell that they are the same
				val dirtySmells = patchedSmells.filter { it.isDirty }
				dirtySmells.forEach { it.killedIn(versionable) }
				val patchedCleanSmells = patchedSmells.minus(dirtySmells)
				val (removedSmells, astMappedSmells) = mapUnchangedSmells(patchedCleanSmells, notMappedSmellsInNewFile)
				astMappedSmells.forEach { it.addWeight(1) } // modified smells get extra weight
				// Remaining not found smells in the AST were deleted
				removedSmells.forEach { it.killedIn(versionable) }
				// As they are truly new smells in modified file -> add to container
				var newSmells = notMappedSmellsInNewFile.minus(astMappedSmells)
				if (newSmells.isNotEmpty() && fileChange.isOfType(FileChange.Type.RELOCATION)) {
					// resurrected smells which are now relocated are not found by compareString method
					val relocatedDeadSmellsInOldFile = before.dead()
					val resurrectedSmells = mapUnchangedSmells(relocatedDeadSmellsInOldFile, newSmells)
					{ it, findSmell -> astMapping.compare.matchesRelocated(it, findSmell) }.second
					newSmells = newSmells.minus(resurrectedSmells)
				}
				newSmells.forEach {
					it.applyVersion(versionable)
					before.addSmell(it)
				}
			}
		}
	}

	private fun handleDeleted(versionable: Versionable,
							  before: SmellContainer<JavaCodeSmell>,
							  changes: List<FileChange>) {
		val deletedSmells = changes.asSequence()
				.map { it.oldFile() }
				.map { it.path() }
				.map { before.findBySourcePath(it).asSequence().filter { it.isAlive } }
				.flatMap { it }
		deletedSmells.forEach { it.killedIn(versionable) }
	}

	private fun handleAdditions(versionable: Versionable,
								before: SmellContainer<JavaCodeSmell>,
								after: SmellContainer<JavaCodeSmell>,
								changes: List<FileChange>) {
		val newSmells = changes.asSequence()
				.map { it.newFile() }
				.map { it.path() }
				.map { after.findBySourcePath(it) }
				.flatMap { it.asSequence() }
		newSmells.forEach { it.applyVersion(versionable) }
		before.all().addAll(newSmells)
	}
}
