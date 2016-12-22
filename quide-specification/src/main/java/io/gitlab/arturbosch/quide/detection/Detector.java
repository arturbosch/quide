package io.gitlab.arturbosch.quide.detection;

import io.gitlab.arturbosch.quide.model.SmellContainer;
import io.gitlab.arturbosch.quide.platform.Nameable;
import io.gitlab.arturbosch.quide.platform.UserData;

/**
 * @author Artur Bosch
 */
public interface Detector<S extends SmellContainer> extends Nameable {

	<U extends UserData> S execute(U data);
}
