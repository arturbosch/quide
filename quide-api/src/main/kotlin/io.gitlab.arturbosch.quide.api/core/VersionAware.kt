package io.gitlab.arturbosch.quide.api.core

import io.gitlab.arturbosch.quide.vcs.VersionProvider
import io.gitlab.arturbosch.quide.vcs.Versionable

/**
 * @author Artur Bosch
 */
interface VersionAware : StorageAware {

	val lastContainer get(): Versionable? = get(LAST_CONTAINER)
	val currentContainer get(): Versionable? = get(CURRENT_CONTAINER)
	val lastVersion get(): Versionable? = get(LAST_VERSION)
	val currentVersion get(): Versionable? = get(CURRENT_VERSION)
	val versionProvider get(): VersionProvider? = get(VERSION_PROVIDER)
	val isEvolutionaryAnalysis get(): Boolean = versionProvider != null

	companion object {
		const val LAST_CONTAINER = "lastContainer"
		const val CURRENT_CONTAINER = "currentContainer"
		const val LAST_VERSION = "lastVersion"
		const val CURRENT_VERSION = "currentVersion"
		const val VERSION_PROVIDER = "versionProviderRegistered"
	}
}
