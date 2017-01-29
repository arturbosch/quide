package io.gitlab.arturbosch.quide.java.mapping

import com.github.javaparser.ast.CompilationUnit
import io.gitlab.arturbosch.quide.java.JavaCodeSmell
import io.gitlab.arturbosch.quide.vcs.Patch

/**
 * @author Artur Bosch
 */
class ASTPatch(val oldUnit: CompilationUnit, val newUnit: CompilationUnit) : Patch<JavaCodeSmell> {

	override fun patchSmell(smell: JavaCodeSmell): JavaCodeSmell {
		return smell
	}

}