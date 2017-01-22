package io.gitlab.arturbosch.quide.platform;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Artur Bosch
 */
public interface QuideDirectory {

	default Path checkDir(Path path) {
		if (Files.notExists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}
		return path;
	}

	default Path home() {
		return checkDir(Paths.get(System.getProperty("user.home"), ".quide"));
	}

	default Path pluginsDir() {
		return checkDir(home().resolve("plugins"));
	}

	default Path configurationsDir() {
		return checkDir(home().resolve("configurations"));
	}
}
