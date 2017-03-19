package io.gitlab.arturbosch.quide.model;

/**
 * Models which are meant to be printed to the console should have a compact string representation.
 *
 * @author Artur Bosch
 */
public interface Printable {

	/**
	 * Converts this object to a compact string representation.
	 * Default is just the toString method call.
	 *
	 * @return string representation of this object
	 */
	default String asPrintable() {
		return this.toString();
	}

}
