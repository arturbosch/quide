package io.gitlab.arturbosch.quide.detection;

/**
 * @author Artur Bosch
 */
public interface Tool {

	default String name() {
		return getClass().getSimpleName();
	}

}
