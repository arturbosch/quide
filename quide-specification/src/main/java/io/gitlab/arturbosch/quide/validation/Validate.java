package io.gitlab.arturbosch.quide.validation;

/**
 * @author Artur Bosch
 */
public class Validate {

	private Validate(){

	}

	public static <T> T notNull(T reference) {
		if (reference == null) throw new NullPointerException("Provided argument was null!");
		return reference;
	}
}
