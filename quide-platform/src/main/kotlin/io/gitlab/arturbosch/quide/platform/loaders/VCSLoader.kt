package io.gitlab.arturbosch.quide.platform.loaders

import io.gitlab.arturbosch.quide.vcs.VersionProvider

/**
 * @author Artur Bosch
 */
interface VCSLoader {
	fun load(): VersionProvider?
}
