package io.gitlab.arturbosch.quide.platform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
				throw new UncheckedIOException("Error creating directories for " + path, e);
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
		return getProperties().get(key);
	}

	Map<String, String> getProperties();

	@SuppressWarnings("unchecked")
	class DefaultQuideDirectory implements QuideDirectory {

		private static final Logger LOGGER = LoggerFactory.getLogger(QuideDirectory.class.getSimpleName());

		protected Map<String, String> properties = new HashMap<>();

		{
			Path propertiesPath = home().resolve("quide.properties");
			try {
				if (Files.notExists(propertiesPath)) {
					Files.createFile(propertiesPath);
				}
				loadProperties(propertiesPath);
				loadAdditionalProperties();
				LOGGER.info("Properties loaded");
			} catch (IOException e) {
				throw new UncheckedIOException("Error loading quide properties", e);
			}
		}

		private void loadAdditionalProperties() {
			String additional = getProperty("additional.properties");
			if (additional != null) {
				Arrays.stream(additional.split(","))
						.map(String::trim)
						.map(Paths::get)
						.forEach(this::loadProperties);
			}
		}

		protected void loadProperties(Path propertiesPath) {
			try (InputStream is = Files.newInputStream(propertiesPath)) {
				Properties props = new Properties();
				props.load(is);
				properties.putAll((Map) props);
			} catch (IOException e) {
				throw new UncheckedIOException("Error loading quide properties in " + propertiesPath, e);
			}
		}

		@Override
		public Map<String, String> getProperties() {
			return properties;
		}
	}
}
