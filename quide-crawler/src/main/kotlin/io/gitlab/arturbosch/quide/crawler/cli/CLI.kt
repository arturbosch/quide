package io.gitlab.arturbosch.quide.crawler.cli

import io.gitlab.arturbosch.kutils.readLines
import io.gitlab.arturbosch.quide.crawler.Console
import io.gitlab.arturbosch.quide.crawler.pipe.GitPipe

/**
 * @author Artur Bosch
 */
object CLI {

	fun run(args: Args) {
		with(args) {
			require(input != null) { "Unexpected null input file!" }
			require(output != null) { "Unexpected null output directory!" }
			val gitEntries = input!!.readLines()
			Console.write("Start piping to $output...")
			GitPipe.start(output!!, gitEntries)
			Console.write("Finished piping to $output!")
		}
	}
}