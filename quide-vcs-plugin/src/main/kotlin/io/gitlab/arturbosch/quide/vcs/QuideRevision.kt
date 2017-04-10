package io.gitlab.arturbosch.quide.vcs

import io.gitlab.arturbosch.kutils.toZonedDateTime
import org.vcsreader.VcsCommit
import java.time.ZonedDateTime

/**
 * @author Artur Bosch
 */
data class QuideRevision(private val commit: VcsCommit) : Revision {
	override fun versionHash(): String = commit.revision
	override fun parentHash(): String = commit.revisionBefore
	override fun message(): String = commit.message
	override fun author(): String = commit.author
	override fun date(): ZonedDateTime = commit.time.toZonedDateTime()
	override fun isMerge(): Boolean = false
}