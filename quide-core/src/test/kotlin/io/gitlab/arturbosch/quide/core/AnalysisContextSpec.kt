package io.gitlab.arturbosch.quide.core

import io.gitlab.arturbosch.quide.TestFileSystem
import io.gitlab.arturbosch.quide.TestQuideDir
import io.gitlab.arturbosch.quide.TestStorage
import io.gitlab.arturbosch.quide.baseDir
import io.gitlab.arturbosch.quide.core.context.DefaultAnalysisContext
import io.gitlab.arturbosch.quide.homeDir
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

/**
 * @author Artur Bosch
 */
class AnalysisContextSpec : Spek({

	describe("an analysis context manages the scope of a plugin") {

		val context = DefaultAnalysisContext(
				"Test",
				TestStorage,
				TestFileSystem,
				TestQuideDir)

		on("delegated behaviour") {

			it("should behave like a storage") {
				context.put("key", "value")
				assertThat(context.get<String>("key")).isEqualTo("value")
			}
			it("should behave like a filesystem") {
				val base = context.inputDir(baseDir)
				assertThat(base!!.file).isEqualTo(baseDir)
			}
			it("should behave like a quide directory") {
				assertThat(context.home).isEqualTo(homeDir)
			}
		}
	}
})
