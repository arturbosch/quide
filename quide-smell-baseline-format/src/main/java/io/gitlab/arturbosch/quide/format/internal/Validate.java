package io.gitlab.arturbosch.quide.format.internal;

/**
 * @author Artur Bosch
 */
public final class Validate {

	private Validate() {
	}

	public static <T> T notNull(T object) {
		if (object == null) {
			throw new IllegalArgumentException("Given parameter must not be null!");
		}
		return object;
	}
}
