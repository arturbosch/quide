package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.quide.java.core.JavaCodeSmell
import io.gitlab.arturbosch.quide.java.core.JavaSmellContainer
import io.gitlab.arturbosch.quide.platform.ControlFlow
import io.gitlab.arturbosch.quide.platform.QuideConstants
import io.gitlab.arturbosch.quide.platform.QuideDirectory
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.quide.vcs.Versionable
import java.nio.file.Path
import java.util.Optional

/**
 * @author Artur Bosch
 */

const val FACADE: String = "FACADE"
const val UPDATABLE_FACADE: String = "UPDATABLE_FACADE"
const val UPDATABLE_STORAGE: String = "UPDATABLE_STORAGE"
const val JAVA = ".java"

const val PATHS_FILTERS_JAVA = "path.filters.java"
const val OUTPUT_JAVA_XML = "output.java.xml"

const val PLUGIN_JAVA_CONFIG = "plugin.java.config"

fun UserData.safeContainer(): Optional<JavaSmellContainer> = currentContainer<JavaSmellContainer, JavaCodeSmell>()

fun UserData.withOutputPath(block: (Path, Versionable, JavaSmellContainer) -> Unit) {
	outputPath().ifPresent { output ->
		val currentVersion = currentVersion()
		val currentContainer = safeContainer()
		if (currentVersion.isPresent && currentContainer.isPresent) {
			val (version, container) = currentVersion.get() to currentContainer.get()
			block(output, version, container)
		} else {
			ControlFlow.LOGGER.warn("Invalid state! Current container=${currentContainer.isPresent} " +
					"and version=${currentVersion.isPresent}")
		}
	}
}

fun loadFiltersFromProperties(quideDirectory: QuideDirectory): List<String> {
	val globalFilters = quideDirectory.getPropertyOrDefault(QuideConstants.PATHS_FILTERS_GLOBAL, "").trim()
	val javaFilters = quideDirectory.getPropertyOrDefault(PATHS_FILTERS_JAVA, "").trim()
	return globalFilters.split(',').plus(javaFilters.split(',')).filterNot(String::isNullOrBlank)
}
