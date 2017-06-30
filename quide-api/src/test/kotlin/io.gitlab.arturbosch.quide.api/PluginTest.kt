package io.gitlab.arturbosch.quide.api

import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

/**
 * @author Artur Bosch
 */
class PluginTest : Spek({

	describe("plugin lifecycle") {

		describe("constructing a test plugin") {

			val context = Plugin.Context()
			TestPlugin.define(context)

			it("should have the name 'TestPlugin' and id 'test'") {
				Assertions.assertThat(TestPlugin.id).isEqualTo("test")
				Assertions.assertThat(TestPlugin.name).isEqualTo("TestPlugin")
			}

			it("should return provided detectors and processors") {
				Assertions.assertThat(context.detectorInstance).isInstanceOf(KotlinTestCodeSmellDetector::class.java)
				Assertions.assertThat(context.registeredProcessors).hasSize(2)
			}
		}
	}

})