package io.gitlab.arturbosch.quide.vcs

import io.gitlab.arturbosch.kutils.toZonedDateTime
import org.vcsreader.VcsCommit
import java.time.ZonedDateTime
import java.util.Date

/**
 * @author Artur Bosch
 */
data class QuideRevision(private val commit: VcsCommit) : Revision {
	override fun versionHash(): String = commit.revision
	override fun parentHash(): String = commit.revisionBefore
	override fun message(): String = commit.message
	override fun author(): String = commit.author
	override fun date(): ZonedDateTime = Date.from(commit.dateTime).toZonedDateTime()
	override fun isMerge(): Boolean = false
}