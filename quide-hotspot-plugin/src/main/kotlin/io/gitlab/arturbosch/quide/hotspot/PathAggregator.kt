package io.gitlab.arturbosch.quide.hotspot

import io.gitlab.arturbosch.quide.platform.ControlFlow
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.quide.platform.processors.EvolutionaryProcessor
import io.gitlab.arturbosch.quide.vcs.FileChange
import io.gitlab.arturbosch.quide.vcs.Versionable

/**
 * @author Artur Bosch
 */
class PathAggregator : EvolutionaryProcessor {

	override fun <U : UserData> doIfActive(data: U) {
		data.currentVersion().orElse(null)?.let {
			reportModifiedPaths(data as HotspotData, it)
		} ?: throw IllegalStateException("Expected a version to analyze!")
	}

	private fun reportModifiedPaths(data: HotspotData, version: Versionable) {
		val paths = data.paths
		version.fileChanges().forEach {
			when (it.type()) {
				FileChange.Type.ADDITION -> paths.put(it.newFile().path(), 0)
				FileChange.Type.MODIFICATION -> paths.merge(it.newFile().path(), 1) { old, new -> old + new }
				FileChange.Type.REMOVAL -> paths.remove(it.oldFile().path())
				FileChange.Type.RELOCATION -> {
					val oldValue = paths.remove(it.oldFile().path())
					val newValue = oldValue?.plus(1) ?: 1
					paths.put(it.newFile().path(), newValue)
				}
				else -> {
					// java enum can be null - fix/ignore
				}
			}
		}
	}

	override fun injectionPoint(): ControlFlow.InjectionPoint {
		return ControlFlow.InjectionPoint.AfterDetection
	}

}