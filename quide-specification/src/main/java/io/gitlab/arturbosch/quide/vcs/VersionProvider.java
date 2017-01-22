package io.gitlab.arturbosch.quide.vcs;

import io.gitlab.arturbosch.quide.platform.AnalysisContext;
import io.gitlab.arturbosch.quide.platform.Nameable;

import java.util.Optional;

/**
 * @author Artur Bosch
 */
public interface VersionProvider extends Nameable {

	/**
	 * As a version provider can be used as a plugin, initialization
	 * outside a constructor is the preferred way.
	 *
	 * @param context provides information about ongoing analysis
	 */
	void initialize(AnalysisContext context);

	Optional<Versionable> nextVersion();

}
