package io.gitlab.arturbosch.quide.platform;

/**
 * @author Artur Bosch
 */
public interface Processor extends Executable {

	default ControlFlow.InjectionPoint injectionPoint() {
		return ControlFlow.InjectionPoint.AfterAnalysis;
	}

	default int priority() {
		return 0;
	}
}
