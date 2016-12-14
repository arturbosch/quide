package io.gitlab.arturbosch.quide.platform;

import io.gitlab.arturbosch.quide.vcs.Versionable;

import java.nio.file.Path;

/**
 * @author Artur Bosch
 */
public interface PreProcessor {
	<T extends Versionable, U extends UserData> void process(T version, Path project, U data);
}
