package io.gitlab.arturbosch.quide.platform;

import java.nio.file.Path;
import java.util.Optional;

/**
 * @author Artur Bosch
 */
public interface AnalysisContext {

	Path projectPath();

	Optional<Path> outputPath();

	QuideDirectory quideDirectory();
}
