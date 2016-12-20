package io.gitlab.arturbosch.quide.vcs;

import io.gitlab.arturbosch.quide.platform.Nameable;

import java.util.Optional;

/**
 * @author Artur Bosch
 */
public interface VersionProvider extends Nameable {

	Optional<Versionable> nextVersion();

}
