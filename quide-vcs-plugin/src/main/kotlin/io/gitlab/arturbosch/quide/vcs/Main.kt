package io.gitlab.arturbosch.quide.vcs

import java.nio.file.Paths

fun main(args: Array<String>) {
	val versionProvider = QuideGitVersionProvider(Paths.get(args[0]))
	var nextVersion = versionProvider.nextVersion()
	while (nextVersion.isPresent) {
		println("${nextVersion.get().versionNumber()} - ${nextVersion.get().revision()}")
		nextVersion = versionProvider.nextVersion()
	}
}