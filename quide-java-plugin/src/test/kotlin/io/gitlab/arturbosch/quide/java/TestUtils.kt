@file:JvmName(name = "Utils")

package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.smartsmells.api.DetectorFacade
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

fun String.lint(): JavaSmellContainer {
	return JavaSmellContainer(DetectorFacade.fullStackFacade().run(this.asResourcePath()))
}

fun String.content(): String {
	return String(Files.readAllBytes(this.asResourcePath()))
}

fun String.asResourcePath(): Path {
	return Paths.get(TestUtils.javaClass.getResource("/$this").file)
}

object TestUtils