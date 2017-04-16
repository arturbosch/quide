package io.gitlab.arturbosch.quide.crawler.cli

import io.gitlab.arturbosch.kutils.readLines
import io.gitlab.arturbosch.quide.crawler.pipe.Pipe

/**
 * @author Artur Bosch
 */
object CLI {

	fun run(args: Args) {
		with(args) {
			require(input != null) { "Unexpected null input file!" }
//			require(output != null) { "Unexpected null output directory!" }
			val gitEntries = input!!.readLines()
			Pipe.start(output!!, gitEntries)
		}
	}
}