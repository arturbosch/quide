package io.gitlab.arturbosch.quide.vcs

/**
 * @author Artur Bosch
 */
data class QuideSourceFile(private val path: String, private val lazyFileContent: () -> String) : SourceFile {
	private val _content = lazy { lazyFileContent.invoke() }
	override fun path(): String = path
	override fun content(): String = _content.value
}
