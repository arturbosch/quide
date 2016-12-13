package io.gitlab.arturbosch.quide.platform;

import io.gitlab.arturbosch.quide.vcs.Versionable;

import java.nio.file.Path;

/**
 * @author Artur Bosch
 */
public interface UserData {
	Versionable currentVersion();
	Path projectPath();
}
