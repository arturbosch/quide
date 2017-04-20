package io.gitlab.arturbosch.quide.crawler.pipe

import io.gitlab.arturbosch.kutils.Try
import io.gitlab.arturbosch.kutils.write
import io.gitlab.arturbosch.quide.crawler.Console
import java.nio.file.Path

/**
 * @author Artur Bosch
 */
object Write {

	fun toFile(path: Path, result: String) {
		val name = path.fileName.toString()
		val resultFile = path.parent.resolve("$name.txt")
		Try {
			resultFile.write(result)
		} onSuccess {
			Console.write("Wrote $name to $it")
		} onError {
			Console.write("Error when writing results for $name: $it")
		}
	}

}