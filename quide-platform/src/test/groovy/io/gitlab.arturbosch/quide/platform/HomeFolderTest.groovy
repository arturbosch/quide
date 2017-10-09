package io.gitlab.arturbosch.quide.platform

import spock.lang.Specification

/**
 * @author Artur Bosch
 */
class HomeFolderTest extends Specification {

	def "load additional property pairs"() {
		when:
		HomeFolder.INSTANCE.addPropertyPairs("test=stuff, next=stuff")
		then:
		HomeFolder.INSTANCE.properties.containsKey("test")
		HomeFolder.INSTANCE.properties.containsKey("next")
	}
}
