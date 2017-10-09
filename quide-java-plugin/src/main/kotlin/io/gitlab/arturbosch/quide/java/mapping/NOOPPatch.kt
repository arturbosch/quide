package io.gitlab.arturbosch.quide.java.mapping

import io.gitlab.arturbosch.quide.java.core.JavaCodeSmell
import io.gitlab.arturbosch.quide.vcs.Patch

/**
 * This patch is used when a compilation unit cannot be created due to
 * compilation errors. The smells are just returned unchanged.
 *
 * @author Artur Bosch
 */
object NOOPPatch : JavaCodeSmellPatch {

	override fun patchSmell(smell: JavaCodeSmell) = smell.apply { markDirty() }
}

interface JavaCodeSmellPatch : Patch<JavaCodeSmell>