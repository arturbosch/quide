package io.gitlab.arturbosch.quide.crawler

import io.gitlab.arturbosch.quide.crawler.cli.CLI
import io.gitlab.arturbosch.quide.crawler.cli.parse

/**
 * @author Artur Bosch
 */
fun main(args: Array<String>) {
	with(args.parse()) {

		val ioNull = input == null && output == null

		if (ioNull) {
			println(usage)
			return
		}

		CLI.run(this)
	}
}
