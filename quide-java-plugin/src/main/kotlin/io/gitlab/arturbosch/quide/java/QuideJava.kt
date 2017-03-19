package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.quide.java.core.JavaCodeSmell
import io.gitlab.arturbosch.quide.java.core.JavaSmellContainer
import io.gitlab.arturbosch.quide.platform.UserData
import java.util.Optional

/**
 * @author Artur Bosch
 */

const val FACADE: String = "FACADE"
const val UPDATABLE_FACADE: String = "UPDATABLE_FACADE"
const val JAVA = ".java"

const val PATHS_FILTERS_JAVA = "path.filters.java"
const val OUTPUT_JAVA_XML = "output.java.xml"

fun UserData.safeContainer(): Optional<JavaSmellContainer> = currentContainer<JavaSmellContainer, JavaCodeSmell>()