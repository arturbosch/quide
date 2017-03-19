package io.gitlab.arturbosch.quide.groovy

import groovy.transform.CompileStatic
import io.gitlab.arturbosch.quide.model.SmellContainer

/**
 * @author Artur Bosch
 */
@CompileStatic
class GroovySmellContainer implements SmellContainer<GroovyCodeSmell> {

	private List<GroovyCodeSmell> smells = []

	@Override
	List<GroovyCodeSmell> all() {
		return smells
	}

}
