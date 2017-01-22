package io.gitlab.arturbosch.quide.platform

import io.gitlab.arturbosch.quide.vcs.VersionProvider
import io.gitlab.arturbosch.quide.vcs.Versionable
import java.util.Optional

/**
 * @author Artur Bosch
 */
class SimpleVersionProvider : VersionProvider {

	private var current = 0

	override fun initialize(context: AnalysisContext) {

	}

	override fun nextVersion(): Optional<Versionable> {
		if (current++ < 5) {
			return Optional.of(DefaultVersion())
		}
		return Optional.empty()
	}
}