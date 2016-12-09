package io.gitlab.arturbosch.quide.mapping;

import io.gitlab.arturbosch.quide.model.CodeSmell;
import io.gitlab.arturbosch.quide.vcs.Patch;
import io.gitlab.arturbosch.quide.vcs.SourceFile;

/**
 * @author Artur Bosch
 */
public interface SmellCompareStrategy {

	boolean matches(CodeSmell first, CodeSmell second);

	CodeSmell patchSmell(CodeSmell smell, SourceFile sourceFile, Patch patch);
}
