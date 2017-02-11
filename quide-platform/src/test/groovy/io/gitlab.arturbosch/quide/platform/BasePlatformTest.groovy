package io.gitlab.arturbosch.quide.platform

import io.gitlab.arturbosch.quide.platform.test.EmptyAnalysis
import io.gitlab.arturbosch.quide.platform.test.TestPluginDetector
import io.gitlab.arturbosch.quide.platform.test.TestVCSLoader
import spock.lang.Specification

/**
 * @author Artur Bosch
 */
class BasePlatformTest extends Specification {

	def "successful platform lifecycle run"() {
		when:
		def platform = new BasePlatform(new EmptyAnalysis(), new BasePluginLoader(new TestPluginDetector()), null)
		def plugins = platform.plugins()

		then:
		plugins.size() == 1
		plugins[0].detector().name() == "SimpleDetector"
	}

	def "successful base version platform lifecycle run"() {
		when:
		def detector = new TestPluginDetector()
		def quide = new QuidePlatform(new EmptyAnalysis(), new TestVCSLoader(), new BasePluginLoader(detector))

		then:
		quide.executablePlatform instanceof BasePlatform
		quide.executablePlatform.multiPlatform == null
	}

}
