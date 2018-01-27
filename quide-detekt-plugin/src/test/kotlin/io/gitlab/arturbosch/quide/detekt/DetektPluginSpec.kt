package io.gitlab.arturbosch.quide.detekt

import io.gitlab.arturbosch.quide.TestContext
import io.gitlab.arturbosch.quide.TestFileSystem
import io.gitlab.arturbosch.quide.api.Plugin
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

/**
 * @author Artur Bosch
 */
class DetektPluginSpec : Spek({

	describe("setting up detekt plugin") {

		val plugin = DetektPlugin()
		val context = Plugin.Context()
		plugin.define(context)

		it("defines detekt detector") {
			assertThat(context.detectorInstance).isInstanceOf(DetektTool::class.java)
		}

		it("generates a report in working directory") {
			context.detectorInstance?.execute(TestContext(plugin))
			val reportFile = TestFileSystem.workDir.resolve("detekt-checkstyle.xml")
			assertThat(reportFile.exists()).isTrue()
		}
	}
})
