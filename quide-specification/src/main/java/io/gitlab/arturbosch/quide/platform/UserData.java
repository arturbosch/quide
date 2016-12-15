package io.gitlab.arturbosch.quide.platform;

import io.gitlab.arturbosch.quide.model.CodeSmell;
import io.gitlab.arturbosch.quide.model.SmellContainer;
import io.gitlab.arturbosch.quide.platform.reflect.TypeToken;
import io.gitlab.arturbosch.quide.vcs.Versionable;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Artur Bosch
 */
public abstract class UserData implements AnalysisAware {

	protected Map<String, Object> storage = new ConcurrentHashMap<>();

	@Override
	public Optional<Versionable> lastVersion() {
		return get("lastVersion", TypeToken.get(Versionable.class));
	}

	@Override
	public Optional<Versionable> currentVersion() {
		return get("lastVersion", TypeToken.get(Versionable.class));
	}

	@Override
	public Optional<SmellContainer<CodeSmell>> lastContainer() {
		TypeToken<SmellContainer<CodeSmell>> typeToken =
				new TypeToken<SmellContainer<CodeSmell>>() {
				};
		return get("lastContainer", typeToken);
	}

	@Override
	public Optional<SmellContainer<CodeSmell>> currentContainer() {
		TypeToken<SmellContainer<CodeSmell>> typeToken =
				new TypeToken<SmellContainer<CodeSmell>>() {
				};
		return get("currentContainer", typeToken);
	}

	@Override
	public Optional<Path> projectPath() {
		return get("projectPath", TypeToken.get(Path.class));
	}

	@SuppressWarnings("unchecked")
	public <T> Optional<T> get(String key, TypeToken<T> typeToken) {
		Type type = typeToken.getType();
		Object value = storage.get(key);
		if (value != null && value.getClass().equals(type)) {
			return Optional.of((T) value);
		} else {
			return Optional.empty();
		}
	}

	public <T> void put(String key, T value) {
		storage.put(key, value);
	}

}
