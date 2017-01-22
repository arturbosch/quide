package io.gitlab.arturbosch.quide.platform

import io.gitlab.arturbosch.quide.platform.test.EmptyAnalysis
import io.gitlab.arturbosch.quide.platform.test.TestPluginDetector
import io.gitlab.arturbosch.quide.vcs.VersionProvider
import io.gitlab.arturbosch.quide.vcs.Versionable
import spock.lang.Specification

/**
 * @author Artur Bosch
 */
class MultiPlatformTest extends Specification {

	def "test"() {
		when:
		def detector = new TestPluginDetector()
		def platform = new MultiPlatform(
				new BasePlatform(new EmptyAnalysis(), new BasePluginLoader(detector)),
				new SameVersionProvider())
		platform.analyze()
		then:
		platform.platform.plugins()[0].userData().lastVersion().isPresent()
		platform.platform.plugins()[0].userData().currentVersion().isPresent()
	}

	class SameVersionProvider implements VersionProvider {
		private int current = 0

		@Override
		Optional<Versionable> nextVersion() {
			if (current++ < 5) {
				return Optional.of(new DefaultVersion())
			}
			return Optional.empty()
		}
	}
}
