package io.gitlab.arturbosch.quide.platform;

/**
 * @author Artur Bosch
 */
public interface Nameable {

	default String name() {
		return getClass().getSimpleName();
	}

}
