package io.gitlab.arturbosch.quide.format

import io.gitlab.arturbosch.quide.format.xml.Blacklist
import io.gitlab.arturbosch.quide.format.xml.SmellBaseline
import io.gitlab.arturbosch.quide.format.xml.Whitelist
import spock.lang.Specification

import java.nio.file.Files
import java.time.Instant

/**
 * @author Artur Bosch
 */
class BaselineFormatTest extends Specification {

	def file = new File("./src/main/resources/bla.xml").toPath()

	def cleanup() {
		Files.deleteIfExists(file)
	}

	def "saved and loaded format must be same"() {

		when: "testing black- and whitelist's"
		def format = new BaselineFormat()
		def type = new SmellBaseline(new Blacklist(["hi", "bye"], "123"), new Whitelist(["www"], "321"))
		format.write(type, file)
		def baseline = format.read(file)

		then: "same amounts of ids"
		baseline.blacklist.ids.size() == 2
		baseline.whitelist.ids.size() == 1
	}

	def "xml domain objects"() {
		when:
		def blacklist = new Blacklist(["1", "2"], "123")
		def newBlacklist = Blacklist.withNewTimestamp("321", blacklist)
		def whitelist = new Whitelist(["1", "2"], "123")
		def newWhitelist = Whitelist.withNewTimestamp("321", whitelist)
		then:
		newBlacklist.ids == ["1", "2"]
		newBlacklist.timestamp == "321"
		newWhitelist.ids == ["1", "2"]
		newWhitelist.timestamp == "321"
		then:
		blacklist.isOlderThan(Instant.now())
		whitelist.isOlderThan(Instant.now())
	}
}
