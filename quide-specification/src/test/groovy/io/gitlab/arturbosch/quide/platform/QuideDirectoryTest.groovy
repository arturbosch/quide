package io.gitlab.arturbosch.quide.platform

import spock.lang.Specification

import java.nio.file.Paths

/**
 * @author Artur Bosch
 */
class QuideDirectoryTest extends Specification {

	def "load additional properties"() {
		given:
		def dir = new QuideDirectory.DefaultQuideDirectory()
		def path = Paths.get(getClass().getResource("/additional.properties").path)
		when:
		dir.loadProperties(path)
		then:
		dir.getProperty("test") == "quide"
	}
}
