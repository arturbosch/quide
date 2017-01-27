package io.gitlab.arturbosch.quide.platform;

import io.gitlab.arturbosch.quide.platform.reflect.TypeToken;

import java.nio.file.Path;
import java.util.Optional;

/**
 * @author Artur Bosch
 */
public interface AnalysisAware extends AnalysisContext, StorageAware {

	String OUTPUT_PATH = "outputPath";
	String PROJECT_PATH = "projectPath";
	String TOOL_NAME = "toolName";
	String QUIDE_DIRECTORY = "quideDirectory";

	default Optional<Path> outputPath() {
		return get(OUTPUT_PATH, TypeToken.get(Path.class));
	}

	default Path projectPath() {
		return get(PROJECT_PATH, TypeToken.get(Path.class))
				.orElseThrow(ProjectPathUnspecifiedError::new);
	}

	default String toolName() {
		return get(TOOL_NAME, TypeToken.get(String.class))
				.orElseThrow(ToolNameUnSpecifiedError::new);
	}

	default QuideDirectory quideDirectory() {
		return get(QUIDE_DIRECTORY, TypeToken.get(QuideDirectory.class))
				.orElseThrow(NoQuideHomeDirectoryError::new);
	}

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
