package io.gitlab.arturbosch.quide.platform;

import io.gitlab.arturbosch.quide.model.CodeSmell;
import io.gitlab.arturbosch.quide.model.SmellContainer;
import io.gitlab.arturbosch.quide.platform.reflect.TypeToken;
import io.gitlab.arturbosch.quide.vcs.Versionable;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Artur Bosch
 */
public abstract class UserData implements AnalysisAware {

	protected Map<String, Object> storage = new ConcurrentHashMap<>();

	public static final String LAST_CONTAINER = "lastContainer";
	public static final String CURRENT_CONTAINER = "currentContainer";
	public static final String LAST_VERSION = "lastVersion";
	public static final String CURRENT_VERSION = "currentVersion";
	public static final String PROJECT_PATH = "projectPath";

	@Override
	public Optional<Versionable> lastVersion() {
		return get(LAST_VERSION, TypeToken.get(Versionable.class));
	}

	@Override
	public Optional<Versionable> currentVersion() {
		return get(CURRENT_VERSION, TypeToken.get(Versionable.class));
	}

	@Override
	public Optional<SmellContainer<CodeSmell>> lastContainer() {
		TypeToken<SmellContainer<CodeSmell>> typeToken =
				new TypeToken<SmellContainer<CodeSmell>>() {
				};
		return get(LAST_CONTAINER, TypeToken.get(typeToken.getType()));
	}

	@Override
	public Optional<SmellContainer<CodeSmell>> currentContainer() {
		TypeToken<SmellContainer<CodeSmell>> typeToken =
				new TypeToken<SmellContainer<CodeSmell>>() {
				};
		return get(CURRENT_CONTAINER, TypeToken.get(typeToken.getType()));
	}

	@Override
	public Path projectPath() {
		Optional<Path> maybePath = get(PROJECT_PATH, TypeToken.get(Path.class));
		if (maybePath.isPresent()) {
			return maybePath.get();
		} else {
			throw new AnalysisAware.ProjectPathUnspecifiedError();
		}
	}

	@SuppressWarnings({"unchecked", "unused"})
	public <T> Optional<T> get(String key, TypeToken<?> typeToken) {
		Object value = storage.get(key);
		return Optional.ofNullable((T) value);
	}

	public <T> void put(String key, T value) {
		storage.put(key, value);
	}

}
