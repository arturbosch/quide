package io.gitlab.arturbosch.quide.platform;

import io.gitlab.arturbosch.quide.model.CodeSmell;
import io.gitlab.arturbosch.quide.model.SmellContainer;
import io.gitlab.arturbosch.quide.vcs.Versionable;

import java.nio.file.Path;

/**
 * @author Artur Bosch
 */
public interface UserData {
	Versionable lastVersion();
	Versionable currentVersion();
	SmellContainer<CodeSmell> lastContainer();
	Path projectPath();
}
