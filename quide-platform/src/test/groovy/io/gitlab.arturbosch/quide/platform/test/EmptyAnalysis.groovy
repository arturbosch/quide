package io.gitlab.arturbosch.quide.platform.test

import io.gitlab.arturbosch.quide.platform.Analysis
import io.gitlab.arturbosch.quide.platform.QuideDirectory

import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Artur Bosch
 */
class EmptyAnalysis implements Analysis {
	@Override
	Path projectPath() {
		return Paths.get(".")
	}

	@Override
	Optional<Path> outputPath() {
		return Optional.of(Paths.get("."))
	}

	@Override
	QuideDirectory quideDirectory() {
	}
}
