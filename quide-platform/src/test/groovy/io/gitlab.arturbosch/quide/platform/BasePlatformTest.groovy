package io.gitlab.arturbosch.quide.platform

import io.gitlab.arturbosch.quide.vcs.VersionProvider
import spock.lang.Specification

/**
 * @author Artur Bosch
 */
class BasePlatformTest extends Specification {

	def "successful platform lifecycle run"() {
		when:
		def platform = new BasePlatform(new BasePluginLoader(new TestPluginDetector()))
		def plugins = platform.plugins()

		then:
		plugins.size() == 1
		plugins[0].detector().name() == "SimpleDetector"
	}

	def "successful base version platform lifecycle run"() {
		when:
		def detector = new TestPluginDetector()
		def platform = new BasePlatform(new BasePluginLoader(detector))
		def quide = new Quide(new TestVCSLoader(), platform)

		then:
		quide.executablePlatform instanceof BasePlatform
	}

	def "successful multiple version platform lifecycle run"() {
		when:
		def detector = new TestPluginDetector()
		def platform = new BasePlatform(new BasePluginLoader(detector))
		def quide = new Quide(new BaseVCSLoader(detector), platform)

		then:
		quide.executablePlatform instanceof MultiPlatform
	}

	class TestVCSLoader implements VCSLoader {
		@Override
		VersionProvider load() {
			return null
		}
	}

	class TestPluginDetector implements PluginDetector {
		@Override
		URL[] getJars() {
			return []
		}
	}
}
