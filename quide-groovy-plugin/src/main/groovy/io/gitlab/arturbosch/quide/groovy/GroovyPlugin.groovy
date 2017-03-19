package io.gitlab.arturbosch.quide.groovy

import groovy.transform.CompileStatic
import io.gitlab.arturbosch.quide.detection.Detector
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

	private UserData container = new UserData() {}

	@Override
	Detector detector() {
		return new CodeNarcTool()
	}

	@Override
	List<Processor> processors() {
		return [new NumberOfSmellsProcessor(), new ResultPrintProcessor()]
	}

	@Override
	UserData userData() {
		return container
	}
}
