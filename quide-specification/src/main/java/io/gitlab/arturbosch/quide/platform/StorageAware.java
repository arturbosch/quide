package io.gitlab.arturbosch.quide.platform;

import io.gitlab.arturbosch.quide.platform.reflect.TypeToken;

import java.util.Optional;

/**
 * @author Artur Bosch
 */
public interface StorageAware extends Storage {

	@SuppressWarnings({"unchecked", "unused"})
	default <T> Optional<T> get(String key, TypeToken<T> typeToken) {
		Object value = storage().get(key);
		return Optional.ofNullable((T) value);
	}

	default <T> void put(String key, T value) {
		storage().put(key, value);
	}

}
