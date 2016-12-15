package io.gitlab.arturbosch.quide.platform

import io.gitlab.arturbosch.quide.detection.Detector
import io.gitlab.arturbosch.quide.mapping.SmellCompareStrategy
import io.gitlab.arturbosch.quide.mapping.SmellMapping
import io.gitlab.arturbosch.quide.model.CodeSmell
import io.gitlab.arturbosch.quide.model.SmellContainer
import io.gitlab.arturbosch.quide.vcs.DiffTool
import io.gitlab.arturbosch.quide.vcs.Versionable

/**
 * @author Artur Bosch
 */
class SimplePlugin : Plugin {

	private val storage = Storage

	override fun <T : CodeSmell> detector(): Detector<T> {
		return object : Detector<T> {
			override fun name(): String {
				return "SimpleDetector"
			}
			override fun <U : UserData?> execute(data: U) {
			}
		}
	}

	override fun processors(): MutableList<Processor> {
		return mutableListOf()
	}

	override fun <T : CodeSmell?> mapping(): SmellMapping<T> {
		return object : SmellMapping<T> {
			override fun map(versionable: Versionable?, before: SmellContainer<T>?, after: SmellContainer<T>?): SmellContainer<T> {
				throw UnsupportedOperationException("not implemented")
			}

			override fun <U : UserData?> execute(data: U) {
			}

			override fun compareAlgorithm(): SmellCompareStrategy<T> {
				throw UnsupportedOperationException("not implemented")
			}

			override fun diffTool(): DiffTool {
				throw UnsupportedOperationException("not implemented")
			}

		}
	}

	override fun userData(): UserData {
		return storage
	}
}