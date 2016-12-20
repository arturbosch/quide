package io.gitlab.arturbosch.quide.platform

import io.gitlab.arturbosch.quide.vcs.VersionProvider
import io.gitlab.arturbosch.quide.vcs.Versionable
import java.util.Optional

/**
 * @author Artur Bosch
 */
class SimpleVersionProvider : VersionProvider {

	override fun nextVersion(): Optional<Versionable> {
		return Optional.empty()
	}

}