@file:JvmName(name = "Utils")

package io.gitlab.arturbosch.quide.java

import io.gitlab.arturbosch.quide.java.core.JavaSmellContainer
import io.gitlab.arturbosch.quide.vcs.FileChange
import io.gitlab.arturbosch.quide.vcs.Revision
import io.gitlab.arturbosch.quide.vcs.Versionable
import io.gitlab.arturbosch.smartsmells.api.DetectorFacade
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.ZonedDateTime

fun String.lint(): JavaSmellContainer {
	return JavaSmellContainer(DetectorFacade.fullStackFacade().run(this.asResourcePath()))
}

fun String.content(): String {
	return String(Files.readAllBytes(this.asResourcePath()))
}

fun String.asResourcePath(): Path {
	return Paths.get(TestUtils.javaClass.getResource("/$this").file)
}

fun String.asResourceStringPath(): String {
	return asResourcePath().toString()
}

object TestUtils

data class TestVersion(val number: Int) : Versionable {
	override fun versionNumber(): Int = number
	override fun revision() = object : Revision {
		override fun versionHash(): String = "123456"
		override fun parentHash(): String = "654321"
		override fun message(): String = ""
		override fun author(): String = ""
		override fun date(): ZonedDateTime = ZonedDateTime.now()
		override fun isMerge(): Boolean = false
	}

	override fun fileChanges(): MutableList<FileChange> = mutableListOf()
}