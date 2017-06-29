package io.gitlab.arturbosch.quide.hotspot

import io.gitlab.arturbosch.quide.detection.CodeSmellDetector
import io.gitlab.arturbosch.quide.model.CodeSmell
import io.gitlab.arturbosch.quide.model.SmellContainer
import io.gitlab.arturbosch.quide.platform.Plugin
import io.gitlab.arturbosch.quide.platform.Processor
import io.gitlab.arturbosch.quide.platform.UserData

/**
 * @author Artur Bosch
 */
class HotspotPlugin : Plugin {

	private val _processors: MutableList<Processor> = mutableListOf(
			PathAggregator(), HotspotReporter()
	)

	override fun detector(): CodeSmellDetector<NoopContainer> = HotspotDetector
	override fun processors(): MutableList<Processor> = _processors
	override fun userData(): UserData = HotspotData

}

object HotspotDetector : CodeSmellDetector<NoopContainer> {
	override fun <U : UserData> execute(data: U): NoopContainer {
		return NoopContainer
	}

}

object NoopContainer : SmellContainer<CodeSmell> {
	override fun all(): MutableList<CodeSmell> = mutableListOf()
}