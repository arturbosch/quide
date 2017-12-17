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
	private final Set<GroovyCodeSmell> smells

	GroovySmellContainer(Results results) {
		this.results = results
		this.smells = results.violations.collect { new GroovyCodeSmell(it as Violation) }.toSet()
	}

	@Override
	Set<GroovyCodeSmell> all() {
		return smells
	}

}
