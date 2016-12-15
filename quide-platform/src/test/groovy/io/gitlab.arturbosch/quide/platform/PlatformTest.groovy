package io.gitlab.arturbosch.quide.platform

import spock.lang.Specification

/**
 * @author Artur Bosch
 */
class PlatformTest extends Specification {

	def "successful platform lifecycle run"() {
		when:
		def platform = new Platform()
		platform.execute()
		def plugins = platform.plugins()

		then:
		plugins.size() == 1
		plugins[0].detector().name() == "SimpleDetector"
	}
}
