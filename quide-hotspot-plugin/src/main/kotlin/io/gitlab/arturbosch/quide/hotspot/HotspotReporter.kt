package io.gitlab.arturbosch.quide.hotspot

import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.quide.platform.processors.EvolutionaryProcessor
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.HashMap

/**
 * @author Artur Bosch
 */
class HotspotReporter : EvolutionaryProcessor {

	override fun <U : UserData> doIfActive(data: U) {
		val projectPath = data.projectPath()
		val paths = (data as HotspotData).paths

		val fileReport = HotspotReport(paths.map { HotSpot(it.key, it.value) })
		val packageReport = packageData(projectPath, paths)

		println("\nHotspot report: \n")
		println(packageReport.textReport())
		data.outputPath().ifPresent {
			val packagePath = it.resolve(projectPath.fileName.toString() + ".hotspot-packages.txt")
			val filePath = it.resolve(projectPath.fileName.toString() + ".hotspot-files.txt")
			fileReport.save(filePath)
			packageReport.save(packagePath)
		}
		println()
	}

	private fun packageData(projectPath: Path, paths: HashMap<String, Int>): HotspotReport {
		val packages = HashMap<String, Int>()
		paths.forEach { path, activity ->
			val relative = projectPath.relativize(Paths.get(path))
			var parent = relative.parent
			while (parent != null) {
				packages.merge(parent.toString(), activity) { old, new -> old + new }
				parent = parent.parent
			}
		}

		val packageKeys = packages.keys
		val hotspots = packages
				.filterKeys { key -> packageKeys.find { key != it && it.contains(key) } == null }
				.map { HotSpot(it.key, it.value) }

		return HotspotReport(hotspots)
	}
}

data class HotSpot(val path: String, val usage: Int)

data class HotspotReport(val hotSpots: List<HotSpot>) {
	private val report = hotSpots.sortedBy { it.usage }
			.asReversed()
			.joinToString("\n") { "${it.path},${it.usage}" }

	fun textReport(): String = report
	fun save(path: Path) {
		Files.write(path, textReport().toByteArray())
	}
}
