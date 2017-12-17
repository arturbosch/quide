package io.gitlab.arturbosch.quide.platform

import io.gitlab.arturbosch.quide.model.BaseCodeSmell
import io.gitlab.arturbosch.quide.model.CodeSmell
import io.gitlab.arturbosch.quide.model.SmellContainer
import io.gitlab.arturbosch.quide.vcs.FileChange
import io.gitlab.arturbosch.quide.vcs.Revision
import io.gitlab.arturbosch.quide.vcs.Versionable
import java.time.ZonedDateTime

/**
 * @author Artur Bosch
 */

object DefaultContainer : SmellContainer<CodeSmell> {
	override fun all(): MutableSet<CodeSmell> {
		return mutableSetOf()
	}

	override fun findBySourcePath(path: String?): MutableList<CodeSmell> {
		return mutableListOf()
	}

}

object DefaultSmell : BaseCodeSmell()

class DefaultVersion(val version: Int = generator++) : Versionable {

	companion object {
		private var generator = 0
	}

	override fun versionNumber(): Int {
		return version
	}

	override fun revision(): Revision {
		return DefaultRevision
	}

	override fun fileChanges(): MutableList<FileChange> {
		return mutableListOf()
	}

	override fun toString(): String {
		return "DefaultVersion(version=$version)"
	}


}

object DefaultRevision : Revision {
	override fun versionHash(): String = ""
	override fun parentHash(): String = ""
	override fun message(): String = ""
	override fun author(): String = ""
	override fun date(): ZonedDateTime = ZonedDateTime.now()
	override fun isMerge(): Boolean = false
}
