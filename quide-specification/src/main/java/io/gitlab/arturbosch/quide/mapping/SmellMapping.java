package io.gitlab.arturbosch.quide.mapping;

import io.gitlab.arturbosch.quide.model.CodeSmell;
import io.gitlab.arturbosch.quide.model.SmellContainer;
import io.gitlab.arturbosch.quide.validation.Validate;
import io.gitlab.arturbosch.quide.vcs.DiffTool;
import io.gitlab.arturbosch.quide.vcs.FileChange;
import io.gitlab.arturbosch.quide.vcs.Versionable;

/**
 * Interface for smell mappings.
 *
 * @author Artur Bosch
 */
public interface SmellMapping {

	SmellCompareStrategy compareAlgorithm();

	DiffTool diffTool();

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
	default SmellContainer mapFirstVersion(Versionable versionable, SmellContainer container) {
		Validate.notNull(versionable);
		Validate.notNull(container);
		container.all().forEach(codeSmell -> {
			codeSmell.setStartVersion(versionable);
			codeSmell.setEndVersion(versionable);
		});
		return container;
	}

	/**
	 * Compares the before and after smell container and creates a new smell container respecting
	 * the differences of before and after.
	 *
	 * @param versionable the current version
	 * @param before      the smells of last version
	 * @param after       the smells of this version
	 * @return a new container with mapped smells
	 */
	SmellContainer map(Versionable versionable, SmellContainer before, SmellContainer after);

	default CodeSmell updateSmell(CodeSmell smell, FileChange fileChange) {
		Validate.notNull(smell);
		Validate.notNull(fileChange);
		return compareAlgorithm().patchSmell(smell, fileChange.newFile(), fileChange.patch(diffTool()));
	}

	default void compareSmells(CodeSmell first, CodeSmell second) {
		Validate.notNull(first);
		Validate.notNull(second);
		compareAlgorithm().matches(first, second);
	}
}
