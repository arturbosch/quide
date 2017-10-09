package io.gitlab.arturbosch.quide.shell.compiler

import io.gitlab.arturbosch.quide.shell.loaders.JavaCommandLoader
import kotlin.system.measureTimeMillis

/**
 * @author Artur Bosch
 */

fun main(args: Array<String>) {
	measureTimeMillis {
		val map = JavaCommandLoader().load()
		map.forEach {
			println(JavaCommandExecutor.run(it.value))
		}
	}.apply { println(this) }
}