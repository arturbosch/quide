package io.gitlab.arturbosch.quide.crawler.pipe

import java.nio.file.Path

/**
 * @author Artur Bosch
 */
interface Pipe {
	fun start(cloneRootDir: Path, entries: List<String>)
}