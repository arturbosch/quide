package io.gitlab.arturbosch.quide.groovy

import groovy.transform.CompileStatic
import io.gitlab.arturbosch.quide.model.BaseCodeSmell
import org.codenarc.rule.Violation

/**
 * @author Artur Bosch
 */
@CompileStatic
class GroovyCodeSmell extends BaseCodeSmell {

	private final Violation violation

	GroovyCodeSmell(Violation violation) {
		this.violation = violation
	}

	@Override
	String asPrintable() {
		return violation.toString()
	}
}
