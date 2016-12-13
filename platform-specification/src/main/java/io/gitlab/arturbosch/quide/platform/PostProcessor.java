package io.gitlab.arturbosch.quide.platform;

import io.gitlab.arturbosch.quide.model.SmellContainer;

/**
 * @author Artur Bosch
 */
public interface PostProcessor {
	<T extends SmellContainer, U extends UserData> void process(T container, U data);
	default ControlFlow.InjectionPoint flow() {
		return ControlFlow.InjectionPoint.AfterAnalysis;
	}
}
