package io.gitlab.arturbosch.quide.vcs

import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

fun main(args: Array<String>) {
	val versionProvider = QuideGitVersionProvider()
	var nextVersion = versionProvider.nextVersion()
	while (nextVersion.isPresent) {
		println("${nextVersion.get().versionNumber()} - ${nextVersion.get().revision()}")
		nextVersion = versionProvider.nextVersion()
	}
}

fun LocalDate.toDate(): Date = Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())