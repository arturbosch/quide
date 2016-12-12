package io.gitlab.arturbosch.quide.vcs;

import java.util.Optional;

/**
 * @author Artur Bosch
 */
public interface VersionProvider extends Iterable<Versionable> {

	Optional<Versionable> nextVersion();

}
