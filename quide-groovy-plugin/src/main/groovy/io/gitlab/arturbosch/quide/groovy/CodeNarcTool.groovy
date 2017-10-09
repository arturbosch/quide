package io.gitlab.arturbosch.quide.groovy

import groovy.transform.CompileStatic
import io.gitlab.arturbosch.quide.detection.CodeSmellDetector
import io.gitlab.arturbosch.quide.platform.UserData
import org.codenarc.CodeNarcRunner
import org.codenarc.analyzer.FilesystemSourceAnalyzer
import org.codenarc.results.Results

/**
 * @author Artur Bosch
 */
@CompileStatic
class CodeNarcTool implements CodeSmellDetector<GroovySmellContainer> {

	private static final CodeNarcRunner runner = new CodeNarcRunner()
	private static final FilesystemSourceAnalyzer analyzer = new FilesystemSourceAnalyzer()

	static {
		runner.sourceAnalyzer = analyzer
	}

	@Override
	<U extends UserData> GroovySmellContainer execute(U data) {
		def quide = data.quideDirectory()
		def config = quide.getProperty(QuideGroovy.PLUGIN_GROOVY_CONFIG)
		if (!config) throw new IllegalStateException(
				"A property $QuideGroovy.PLUGIN_GROOVY_CONFIG must be provided to load codenarc configurations!")
		def path = quide.configurationsDir().resolve("codenarc.groovy")
		analyzer.baseDirectory = data.projectPath().toString()
		runner.ruleSetFiles = "file://" + path.toString()
		Results results = runner.execute()
		return new GroovySmellContainer(results)
	}
}
