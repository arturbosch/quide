package io.gitlab.arturbosch.quide

import io.gitlab.arturbosch.quide.api.core.ResourceAware
import io.gitlab.arturbosch.quide.core.fs.DefaultFileSystem
import java.io.File

/**
 * @author Artur Bosch
 */
object TestFileSystem : DefaultFileSystem(baseDir, workDir)

object Resources : ResourceAware

val homeDir = File(Resources.resource("quideDir").toURI())
val baseDir = File(Resources.resource("baseDir").toURI())
val workDir = File(Resources.resource("workDir").toURI())
