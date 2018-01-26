package io.gitlab.arturbosch.quide.api.core

import io.gitlab.arturbosch.quide.TestQuideDir
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

/**
 * @author Artur Bosch
 */
internal class QuideDirectoryTest : Spek({

	describe("navigation inside the quide directory") {

		val quide = TestQuideDir

		it("provides core paths inside quide") {
			assertThat(quide.home.exists()).isTrue()
			assertThat(quide.pluginsDir.exists()).isTrue()
			assertThat(quide.configurationsDir.exists()).isTrue()
		}

		it("allows to retrieve quide.properties") {
			assertThat(quide.property("test.test.test")).isNull()
			assertThat(quide.propertyOrDefault("test.test.test", "Test")).isEqualTo("Test")
		}
	}
})
