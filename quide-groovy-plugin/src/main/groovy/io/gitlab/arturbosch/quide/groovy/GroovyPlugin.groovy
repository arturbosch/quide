package io.gitlab.arturbosch.quide.groovy

import groovy.transform.CompileStatic
import io.gitlab.arturbosch.quide.detection.CodeSmellDetector
import io.gitlab.arturbosch.quide.platform.Plugin
import io.gitlab.arturbosch.quide.platform.Processor
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.quide.platform.processors.NumberOfSmellsProcessor
import io.gitlab.arturbosch.quide.platform.processors.ResultPrintProcessor

/**
 * @author Artur Bosch
 */
@CompileStatic
class GroovyPlugin implements Plugin {

	private final UserData container = new UserData() {}
	private final CodeNarcTool tool = new CodeNarcTool()
	private final List<Processor> processors = [new NumberOfSmellsProcessor(), new ResultPrintProcessor()]

	@Override
	CodeSmellDetector detector() {
		return tool
	}

	@Override
	List<Processor> processors() {
		return processors
	}

	@Override
	UserData userData() {
		return container
	}
}
