package io.gitlab.arturbosch.quide.platform;

import io.gitlab.arturbosch.quide.model.CodeSmell;
import io.gitlab.arturbosch.quide.model.SmellContainer;
import io.gitlab.arturbosch.quide.vcs.Versionable;

import java.util.Optional;

/**
 * @author Artur Bosch
 */
public interface AnalysisAware extends AnalysisContext {

	Optional<Versionable> lastVersion();

	Optional<Versionable> currentVersion();

	Optional<SmellContainer<CodeSmell>> lastContainer();

	Optional<SmellContainer<CodeSmell>> currentContainer();

	String toolName();

	class ProjectPathUnspecifiedError extends IllegalStateException {
		public ProjectPathUnspecifiedError() {
			super("Project path must be specified for plugins usage!");
		}
	}

	class ToolNameUnSpecifiedError extends IllegalStateException {
		public ToolNameUnSpecifiedError() {
			super("Tool name must be specified for plugins usage!");
		}
	}

	class NoQuideHomeDirectoryError extends IllegalStateException {
		public NoQuideHomeDirectoryError() {
			super("There was no instance of QuideDirectory!");
		}
	}
}
