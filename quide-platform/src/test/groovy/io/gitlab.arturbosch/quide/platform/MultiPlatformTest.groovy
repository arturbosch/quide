package io.gitlab.arturbosch.quide.platform

import io.gitlab.arturbosch.quide.platform.test.EmptyAnalysis
import io.gitlab.arturbosch.quide.platform.test.TestPluginDetector
import spock.lang.Specification

/**
 * @author Artur Bosch
 */
class MultiPlatformTest extends Specification {

	def "successful loaded multi platform"() {
		when:
		def detector = new TestPluginDetector()
		def analysis = new EmptyAnalysis()
		def platform = new BasePlatform(analysis, new BasePluginLoader(detector))
		def quide = new QuidePlatform(new BaseVCSLoader(detector, analysis), platform)

		then:
		quide.executablePlatform instanceof MultiPlatform
	}

	def "successful multiple version platform lifecycle run"() {
		when:
		def detector = new TestPluginDetector()
		def analysis = new EmptyAnalysis()
		def basePlatform = new BasePlatform(analysis, new BasePluginLoader(detector))
		def platform = new QuidePlatform(new BaseVCSLoader(detector, analysis), basePlatform)
		platform.analyze()
		then:
		platform.executablePlatform instanceof MultiPlatform
		basePlatform.plugins()[0].userData().currentVersion().isPresent()
		basePlatform.plugins()[0].userData().lastVersion().isPresent()
	}

}
