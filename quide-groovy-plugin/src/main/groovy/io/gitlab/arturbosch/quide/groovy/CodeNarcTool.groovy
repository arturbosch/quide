package io.gitlab.arturbosch.quide.groovy

import groovy.transform.CompileStatic
import io.gitlab.arturbosch.quide.detection.Detector
import io.gitlab.arturbosch.quide.platform.UserData
import org.codenarc.CodeNarcRunner
import org.codenarc.analyzer.FilesystemSourceAnalyzer
import org.codenarc.results.Results

/**
 * @author Artur Bosch
 */
@CompileStatic
class CodeNarcTool implements Detector<GroovySmellContainer> {

	private static final CodeNarcRunner runner = new CodeNarcRunner()

	static {
		runner.sourceAnalyzer = new FilesystemSourceAnalyzer()
	}

	@Override
	<U extends UserData> GroovySmellContainer execute(U data) {
		def path = data.quideDirectory().configurationsDir().resolve("codenarc.groovy")
		runner.ruleSetFiles = path.toString()
		Results results = runner.execute()
		return new GroovySmellContainer(results)
	}
}
