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
@SuppressWarnings("unchecked")
public class DefaultQuideDirectory implements QuideDirectory {

	private static final Logger LOGGER = LoggerFactory.getLogger(QuideDirectory.class.getSimpleName());

	protected Map<String, String> properties = new HashMap<>();

	{
		Path propertiesPath = home().resolve("quide.properties");
		try {
			if (Files.notExists(propertiesPath)) {
				try (InputStream in = getClass().getResourceAsStream("/quide.properties")) {
					Files.copy(in, propertiesPath);
					LOGGER.info("Created default properties set.");
				}
			} else {
				loadProperties(propertiesPath);
				loadAdditionalProperties();
				LOGGER.info("Properties loaded.");
			}
		} catch (IOException e) {
			throw new UncheckedIOException("Error loading quide properties", e);
		}
	}

	private void loadAdditionalProperties() {
		String additional = getProperty(QuideConstants.PLATFORM_ADDITIONAL_PROPERTIES);
		if (additional != null) {
			loadPropertiesFromString(additional);
		}
	}

	protected void loadPropertiesFromString(String commaSeparatedPaths) {
		Arrays.stream(commaSeparatedPaths.split(","))
				.map(String::trim)
				.map(Paths::get)
				.forEach(this::loadProperties);
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
