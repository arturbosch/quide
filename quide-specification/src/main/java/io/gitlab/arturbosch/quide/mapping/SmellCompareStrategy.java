package io.gitlab.arturbosch.quide.mapping;

import io.gitlab.arturbosch.quide.model.CodeSmell;
import io.gitlab.arturbosch.quide.vcs.Patch;
import io.gitlab.arturbosch.quide.vcs.SourceFile;

/**
 * @author Artur Bosch
 */
public interface SmellCompareStrategy<T extends CodeSmell> {

	boolean matches(T first, T second);

	T patchSmell(T smell, SourceFile sourceFile, Patch patch);
}
