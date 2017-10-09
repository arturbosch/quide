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
		def quide = new QuidePlatform(analysis, new BaseVCSLoader(detector, analysis), new BasePluginLoader(detector))

		then:
		quide.executablePlatform.multiPlatform != null
	}

	def "successful multiple version platform lifecycle run"() {
		when:
		def detector = new TestPluginDetector()
		def analysis = new EmptyAnalysis()
		def quide = new QuidePlatform(analysis, new BaseVCSLoader(detector, analysis), new BasePluginLoader(detector))
		quide.analyze()
		then:
		quide.executablePlatform.plugins()[0].userData().currentVersion().isPresent()
		quide.executablePlatform.plugins()[0].userData().lastVersion().isPresent()
	}

}
