package io.gitlab.arturbosch.quide.groovy

import groovy.transform.CompileStatic
import io.gitlab.arturbosch.quide.detection.Detector
import io.gitlab.arturbosch.quide.platform.UserData

/**
 * @author Artur Bosch
 */
@CompileStatic
class CodeNarcTool implements Detector<GroovySmellContainer> {

	@Override
	<U extends UserData> GroovySmellContainer execute(U data) {
		return new GroovySmellContainer()
	}
}
