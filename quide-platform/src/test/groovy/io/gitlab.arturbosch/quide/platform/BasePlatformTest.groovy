package io.gitlab.arturbosch.quide.platform

import io.gitlab.arturbosch.quide.vcs.VersionProvider
import spock.lang.Specification

import java.nio.file.Path

/**
 * @author Artur Bosch
 */
class BasePlatformTest extends Specification {

	def "successful platform lifecycle run"() {
		when:
		def platform = new BasePlatform(new EmptyAnalysis(), new BasePluginLoader(new TestPluginDetector()))
		def plugins = platform.plugins()

		then:
		plugins.size() == 1
		plugins[0].detector().name() == "SimpleDetector"
	}

	def "successful base version platform lifecycle run"() {
		when:
		def detector = new TestPluginDetector()
		def platform = new BasePlatform(new EmptyAnalysis(), new BasePluginLoader(detector))
		def quide = new QuidePlatform(new TestVCSLoader(), platform)

		then:
		quide.executablePlatform instanceof BasePlatform
	}

	def "successful multiple version platform lifecycle run"() {
		when:
		def detector = new TestPluginDetector()
		def platform = new BasePlatform(new EmptyAnalysis(), new BasePluginLoader(detector))
		def quide = new QuidePlatform(new BaseVCSLoader(detector), platform)

		then:
		quide.executablePlatform instanceof MultiPlatform
	}


	class EmptyAnalysis implements Analysis {
		@Override
		Path projectPath() {
			return null
		}

		@Override
		Optional<Path> outputPath() {
			return null
		}

		@Override
		QuideDirectory quideDirectory() {
			return null
		}
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
