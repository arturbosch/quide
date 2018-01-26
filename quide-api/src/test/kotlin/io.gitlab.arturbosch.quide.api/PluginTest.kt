package io.gitlab.arturbosch.quide.api

import io.gitlab.arturbosch.quide.KotlinTestCodeSmellDetector
import io.gitlab.arturbosch.quide.TestPlugin
import io.gitlab.arturbosch.quide.api.processors.InjectionPoint
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

/**
 * @author Artur Bosch
 */
class PluginTest : Spek({

	describe("plugin lifecycle") {

		val context = Plugin.Context()
		val plugin = TestPlugin

		describe("constructing a test plugin") {

			plugin.define(context)

			it("should have the name 'TestPlugin' and id 'test'") {
				assertThat(TestPlugin.id).isEqualTo("test")
				assertThat(TestPlugin.name).isEqualTo("TestPlugin")
			}

			it("should return provided detectors and processors") {
				assertThat(context.detectorInstance).isInstanceOf(KotlinTestCodeSmellDetector::class.java)
				assertThat(context.registeredProcessors).hasSize(2)
			}

			it("should check if processors have default priority and injection point values") {
				val processors = context.registeredProcessors
				assertThat(processors).allMatch {
					it.priority() == 0 && it.injectionPoint() == InjectionPoint.AfterAnalysis
				}
			}
		}
	}

})
