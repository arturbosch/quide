package io.gitlab.arturbosch.quide.platform

import io.gitlab.arturbosch.quide.model.CodeSmell
import io.gitlab.arturbosch.quide.model.SmellContainer
import io.gitlab.arturbosch.quide.vcs.Versionable
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
@Suppress("UNCHECKED_CAST")
class Storage : UserData {

	override fun lastVersion(): Versionable {
		return (userData["lastVersion"] ?: DefaultVersion) as Versionable
	}

	override fun lastContainer(): SmellContainer<CodeSmell>? {
		return (userData["lastContainer"] ?: DefaultContainer) as SmellContainer<CodeSmell>
	}

	private val userData: Map<String, Any> = hashMapOf()

	override fun currentVersion(): Versionable? {
		return (userData["currentVersion"] ?: DefaultVersion) as Versionable
	}

	override fun projectPath(): Path? {
		return userData["currentVersion"] as Path?

	}

}