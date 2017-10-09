package io.gitlab.arturbosch.quide.platform;

/**
 * @author Artur Bosch
 */
public interface Executable {

	<U extends UserData> void execute(U data);
}
