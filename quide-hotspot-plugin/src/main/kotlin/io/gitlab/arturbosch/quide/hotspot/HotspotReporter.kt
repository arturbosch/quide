package io.gitlab.arturbosch.quide.hotspot

import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.quide.platform.processors.EvolutionaryProcessor
import java.nio.file.Path
import java.nio.file.Paths
import java.util.HashMap

/**
 * @author Artur Bosch
 */
class HotspotReporter : EvolutionaryProcessor {

	private val packages = HashMap<String, Int>()

	override fun <U : UserData> doIfActive(data: U) {
		val projectPath = data.projectPath()
		val paths = (data as HotspotData).paths

		splitToPackageStructure(projectPath, paths)
		val packages = distinctSubPackages()

		val result = HotspotResult(projectPath.toString(), packages)
		HotspotData.put(HOTSPOT_RESULT, result)

		println("\nHotspot report: \n")
		println(result.textReport())
		println()
	}

	private fun distinctSubPackages(): List<Package> {
		val packageKeys = packages.keys
		return packages.filterKeys { key -> packageKeys.find { key != it && it.contains(key) } == null }
				.toSortedMap()
				.map { Package(it.key, it.value) }
				.sortedBy { it.usage }
				.reversed()
	}

	private fun splitToPackageStructure(projectPath: Path, paths: HashMap<String, Int>) {
		paths.forEach { path, activity ->
			val relative = projectPath.relativize(Paths.get(path))
			var parent = relative.parent
			while (parent != null) {
				packages.merge(parent.toString(), activity) { old, new -> old + new }
				parent = parent.parent
			}
		}
	}
}

data class Package(val path: String, val usage: Int)

data class HotspotResult(val project: String, val packages: List<Package>) {
	fun textReport(): String = packages.joinToString("\n") { "${it.path} - ${it.usage}" }
}

val HOTSPOT_RESULT = "hotspot.result"