package io.gitlab.arturbosch.quide.platform

import io.gitlab.arturbosch.quide.detection.Detector
import io.gitlab.arturbosch.quide.mapping.SmellMapping
import io.gitlab.arturbosch.quide.model.CodeSmell
import io.gitlab.arturbosch.quide.model.SmellContainer
import io.gitlab.arturbosch.quide.vcs.Versionable

/**
 * @author Artur Bosch
 */
fun main(args: Array<String>) {
	Platform().execute()
}

class Platform : ControlFlow {

	override fun state(): ControlFlow.State {
		return Plugin()
	}

	override fun executeDetection(detector: Detector<CodeSmell>): SmellContainer<CodeSmell> {
		return detector.run(userData())
	}

	override fun executeMapping(versionable: Versionable,
								last: SmellContainer<CodeSmell>,
								current: SmellContainer<CodeSmell>,
								mapping: SmellMapping<CodeSmell>): SmellContainer<CodeSmell> {
		return mapping.map(versionable, last, current)
	}

}