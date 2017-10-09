package io.gitlab.arturbosch.quide.groovy

import groovy.transform.CompileStatic
import io.gitlab.arturbosch.quide.model.SmellContainer
import org.codenarc.results.Results
import org.codenarc.rule.Violation

/**
 * @author Artur Bosch
 */
@CompileStatic
class GroovySmellContainer implements SmellContainer<GroovyCodeSmell> {

	private final Results results
	private final List<GroovyCodeSmell> smells

	GroovySmellContainer(Results results) {
		this.results = results
		this.smells = results.violations.collect { new GroovyCodeSmell(it as Violation) }
	}

	@Override
	List<GroovyCodeSmell> all() {
		return smells
	}

}
