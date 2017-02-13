package io.gitlab.arturbosch.quide.platform;

import io.gitlab.arturbosch.quide.platform.reflect.TypeToken;
import io.gitlab.arturbosch.quide.vcs.VersionProvider;
import io.gitlab.arturbosch.quide.vcs.Versionable;

import java.util.Optional;

/**
 * @author Artur Bosch
 */
public interface VersionAware extends StorageAware {

	String LAST_VERSION = "lastVersion";
	String CURRENT_VERSION = "currentVersion";
	String VERSION_PROVIDER = "versionProviderRegistered";

	default Optional<Versionable> lastVersion() {
		return get(LAST_VERSION, TypeToken.get(Versionable.class));
	}

	default Optional<Versionable> currentVersion() {
		return get(CURRENT_VERSION, TypeToken.get(Versionable.class));
	}

	default Optional<VersionProvider> versionProvider() {
		return get(VERSION_PROVIDER, TypeToken.get(VersionProvider.class));
	}


}
