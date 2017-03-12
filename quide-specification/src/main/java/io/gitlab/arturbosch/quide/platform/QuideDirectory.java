package io.gitlab.arturbosch.quide.platform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

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

	default Path resolve(String subPath) {
		return checkDir(home().resolve(subPath));
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
		return getProperties().getProperty(key);
	}

	Properties getProperties();

	class DefaultQuideDirectory implements QuideDirectory {

		private static final Logger LOGGER = LoggerFactory.getLogger(QuideDirectory.class.getSimpleName());

		private Properties properties = new Properties();

		{
			Path propertiesPath = home().resolve("quide.properties");
			try {
				if (Files.notExists(propertiesPath)) {
					Files.createFile(propertiesPath);
				}
				try (InputStream is = Files.newInputStream(propertiesPath)) {
					properties.load(is);
				}
				LOGGER.info("Properties loaded");
			} catch (IOException e) {
				LOGGER.error("Error loading quide properties", e);
			}
		}

		@Override
		public Properties getProperties() {
			return properties;
		}
	}
}
