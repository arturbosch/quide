package io.gitlab.arturbosch.quide.platform.processors;

import io.gitlab.arturbosch.quide.platform.UserData;

/**
 * @author Artur Bosch
 */
public interface EvolutionaryProcessor extends ConditionalProcessor {

	@Override
	default <U extends UserData> boolean isActive(U data) {
		return data.isEvolutionaryAnalysis();
	}

}
