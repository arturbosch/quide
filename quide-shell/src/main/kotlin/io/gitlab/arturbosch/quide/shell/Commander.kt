package io.gitlab.arturbosch.quide.shell

import io.gitlab.arturbosch.quide.shell.loaders.CommandLoader
import mu.KLogging
import java.util.LinkedHashMap

/**
 * @author Artur Bosch
 */
class Commander(vararg loaders: CommandLoader) : KLogging() {

	private val commands: Map<String, Command> = loaders.map { it.load() }
			.reduce { map, map2 -> map.mergeReduce(map2) }.apply {
		logger.info("Loaded Commands: " + keys.joinToString(", "))
	}

	fun choose(line: String) {
		if (line.isBlank()) return
		commands.keys.find { line.startsWith(it) }
				?.let { println(commands[it]?.run(line.substring(it.length))) }
				?: throw QuideShellException("No matching command found!")
	}

	private fun <K, V> Map<K, V>.mergeReduce(other: Map<K, V>, reduce: (V, V) -> V = { _, b -> b }): Map<K, V> {
		val result = LinkedHashMap<K, V>(this.size + other.size)
		result.putAll(this)
		other.forEach { e -> result[e.key] = result[e.key]?.let { reduce(e.value, it) } ?: e.value }
		return result
	}
}
