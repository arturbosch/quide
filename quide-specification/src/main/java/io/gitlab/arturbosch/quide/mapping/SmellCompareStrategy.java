package io.gitlab.arturbosch.quide.mapping;

import io.gitlab.arturbosch.quide.model.CodeSmell;

/**
 * @author Artur Bosch
 */
public interface SmellCompareStrategy<T extends CodeSmell> {

	boolean matches(T first, T second);

}
