package io.gitlab.arturbosch.quide.platform.test

import io.gitlab.arturbosch.quide.platform.VCSLoader
import io.gitlab.arturbosch.quide.vcs.VersionProvider

/**
 * @author Artur Bosch
 */
class TestVCSLoader implements VCSLoader {
	@Override
	VersionProvider load() {
		return null
	}
}