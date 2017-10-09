package io.gitlab.arturbosch.quide.platform;

import io.gitlab.arturbosch.quide.model.CodeSmell;
import io.gitlab.arturbosch.quide.model.SmellContainer;
import io.gitlab.arturbosch.quide.platform.reflect.TypeToken;

import java.util.Optional;

/**
 * @author Artur Bosch
 */
public interface ContainerAware extends StorageAware {

	String LAST_CONTAINER = "lastContainer";
	String CURRENT_CONTAINER = "currentContainer";

	default <C extends SmellContainer<S>, S extends CodeSmell> Optional<C> lastContainer() {
		TypeToken<C> typeToken = new TypeToken<C>() {
		};
		return get(LAST_CONTAINER, TypeToken.get(typeToken.getType()));
	}

	default <C extends SmellContainer<S>, S extends CodeSmell> Optional<C> currentContainer() {
		TypeToken<C> typeToken = new TypeToken<C>() {
		};
		return get(CURRENT_CONTAINER, TypeToken.get(typeToken.getType()));
	}

}
