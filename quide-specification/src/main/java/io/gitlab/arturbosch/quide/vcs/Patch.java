package io.gitlab.arturbosch.quide.vcs;

import io.gitlab.arturbosch.quide.model.CodeSmell;

/**
 * @author Artur Bosch
 */
public interface Patch<T extends CodeSmell> {

	T patchSmell(T smell);
}
