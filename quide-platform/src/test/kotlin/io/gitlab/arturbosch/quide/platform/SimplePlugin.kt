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

	object Storage : UserData()

	private val storage = Storage

	override fun detector(): Detector<*> {
		return object : Detector<CodeSmell> {
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

	override fun mapping(): SmellMapping<*> {
		return object : SmellMapping<CodeSmell> {
			override fun map(versionable: Versionable, before: SmellContainer<CodeSmell>, after: SmellContainer<CodeSmell>): SmellContainer<CodeSmell> {
				throw UnsupportedOperationException("not implemented")
			}

			override fun <U : UserData?> execute(data: U) {
			}

			override fun compareAlgorithm(): SmellCompareStrategy<CodeSmell> {
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