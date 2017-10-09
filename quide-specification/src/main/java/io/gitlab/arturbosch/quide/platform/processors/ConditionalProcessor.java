package io.gitlab.arturbosch.quide.platform.processors;

import io.gitlab.arturbosch.quide.platform.Processor;
import io.gitlab.arturbosch.quide.platform.UserData;

/**
 * @author Artur Bosch
 */
public interface ConditionalProcessor extends Processor {

	<U extends UserData> boolean isActive(U data);

	@Override
	default <U extends UserData> void execute(U data) {
		if (isActive(data)) {
			doIfActive(data);
		}
	}

	<U extends UserData> void doIfActive(U data);

}
