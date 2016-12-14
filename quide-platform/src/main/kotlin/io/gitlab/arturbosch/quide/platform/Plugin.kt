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
class Plugin : ControlFlow.State {

	private val storage = Storage()

	override fun preProcessors(): MutableList<PreProcessor> {
		return mutableListOf()
	}

	override fun detector(): Detector<CodeSmell> {
		return Detector {
			object : SmellContainer<CodeSmell> {
				override fun all(): MutableList<CodeSmell> {
					throw UnsupportedOperationException("not implemented")
				}

				override fun findBySourcePath(path: String): MutableList<CodeSmell> {
					throw UnsupportedOperationException("not implemented")
				}

			}
		}
	}

	override fun postProcessors(): MutableList<PostProcessor> {
		return mutableListOf()
	}

	override fun mapping(): SmellMapping<CodeSmell> {
		return object : SmellMapping<CodeSmell> {
			override fun compareAlgorithm(): SmellCompareStrategy<CodeSmell> {
				throw UnsupportedOperationException("not implemented")
			}

			override fun diffTool(): DiffTool {
				throw UnsupportedOperationException("not implemented")
			}

			override fun map(versionable: Versionable, before: SmellContainer<CodeSmell>, after: SmellContainer<CodeSmell>): SmellContainer<CodeSmell> {
				return after
			}

		}
	}

	override fun userData(): UserData {
		return storage
	}
}