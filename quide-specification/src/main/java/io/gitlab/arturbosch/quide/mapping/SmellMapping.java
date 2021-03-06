package io.gitlab.arturbosch.quide.mapping;

import io.gitlab.arturbosch.quide.model.CodeSmell;
import io.gitlab.arturbosch.quide.model.SmellContainer;
import io.gitlab.arturbosch.quide.platform.Executable;
import io.gitlab.arturbosch.quide.validation.Validate;
import io.gitlab.arturbosch.quide.vcs.DiffTool;
import io.gitlab.arturbosch.quide.vcs.FileChange;
import io.gitlab.arturbosch.quide.vcs.Patch;
import io.gitlab.arturbosch.quide.vcs.Versionable;

/**
 * Interface for smell mappings.
 *
 * @author Artur Bosch
 */
public interface SmellMapping<T extends CodeSmell> extends Executable {

	SmellCompareStrategy<T> compareAlgorithm();

	DiffTool<? extends Patch<T>> diffTool();

	/**
	 * Compares the before and after smell container and creates a new smell container respecting
	 * the differences of before and after.
	 *
	 * @param versionable the current version
	 * @param before      the smells of last version
	 * @param after       the smells of this version
	 * @return a new container with mapped smells
	 */
	SmellContainer<T> map(Versionable versionable, SmellContainer<T> before, SmellContainer<T> after);

	/**
	 * This method is intended to be used when starting with the first version as only
	 * one smell container is given. In implementation it is recommend to scan all smells
	 * of smell container and set the start and end version to the versionable.
	 * This is done in the default implementation.
	 *
	 * @param versionable the version
	 * @param container   the smell container
	 * @return the same container but with updated version
	 */
	default SmellContainer<T> mapFirstVersion(Versionable versionable, SmellContainer<T> container) {
		Validate.notNull(versionable);
		Validate.notNull(container);
		container.all().forEach(codeSmell -> codeSmell.applyVersion(versionable));
		return container;
	}

	default T updateSmell(T smell, FileChange fileChange) {
		Validate.notNull(smell);
		Validate.notNull(fileChange);
		return fileChange.patch(diffTool()).patchSmell(smell);
	}

	default boolean compareSmells(T first, T second) {
		Validate.notNull(first);
		Validate.notNull(second);
		return compareAlgorithm().matches(first, second);
	}
}
