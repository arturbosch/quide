package io.gitlab.arturbosch.quide.platform;

import io.gitlab.arturbosch.quide.validation.Validate;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @author Artur Bosch
 */
public interface QuideDirectory {

	default Path checkDir(Path path) {
		if (Files.notExists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				throw new UncheckedIOException("Error creating directories for " + path, e);
			}
		}
		return path;
	}

	default Path resolve(String subPath) {
		if (subPath.contains(".")) {
			return checkDir(home()).resolve(subPath);
		} else {
			return checkDir(home().resolve(subPath));
		}
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

	default String getProperty(String key) {
		String property = getProperties().get(key);
		return Validate.isEmpty(property) ? null : property;
	}

	default String getPropertyOrDefault(String key, String defaultValue) {
		String property = getProperties().get(key);
		return Validate.isEmpty(property) ? defaultValue : property;
	}

	Map<String, String> getProperties();

}
