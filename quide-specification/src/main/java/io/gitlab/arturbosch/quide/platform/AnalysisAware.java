package io.gitlab.arturbosch.quide.platform;

import io.gitlab.arturbosch.quide.model.CodeSmell;
import io.gitlab.arturbosch.quide.model.SmellContainer;
import io.gitlab.arturbosch.quide.vcs.Versionable;

import java.nio.file.Path;
import java.util.Optional;

/**
 * @author Artur Bosch
 */
public interface AnalysisAware {
	Optional<Versionable> lastVersion();
	Optional<Versionable> currentVersion();
	Optional<SmellContainer<CodeSmell>> lastContainer();
	Optional<SmellContainer<CodeSmell>> currentContainer();
	Optional<Path> projectPath();
}
