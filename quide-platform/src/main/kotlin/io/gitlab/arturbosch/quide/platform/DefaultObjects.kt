package io.gitlab.arturbosch.quide.platform

import io.gitlab.arturbosch.quide.model.BaseCodeSmell
import io.gitlab.arturbosch.quide.model.SmellContainer
import io.gitlab.arturbosch.quide.vcs.FileChange
import io.gitlab.arturbosch.quide.vcs.Revision
import io.gitlab.arturbosch.quide.vcs.Versionable
import java.time.ZonedDateTime

/**
 * @author Artur Bosch
 */
object DefaultContainer : SmellContainer<DefaultSmell> {
	override fun all(): MutableList<DefaultSmell> {
		return mutableListOf()
	}

	override fun findBySourcePath(path: String?): MutableList<DefaultSmell> {
		return mutableListOf()
	}

}

object DefaultSmell : BaseCodeSmell()

object DefaultVersion : Versionable {
	override fun versionNumber(): Int {
		return -1
	}

	override fun revision(): Revision {
		return DefaultRevision
	}

	override fun fileChanges(): MutableList<FileChange> {
		return mutableListOf()
	}
}

object DefaultRevision : Revision {
	override fun versionHash(): String = ""
	override fun parentHash(): String = ""
	override fun commit(): String = ""
	override fun author(): String = ""
	override fun date(): ZonedDateTime = ZonedDateTime.now()
	override fun isMerge(): Boolean = false
}