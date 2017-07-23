package io.gitlab.arturbosch.quide.vcs

import org.vcsreader.vcs.VcsError

/**
 * @author Artur Bosch
 */
fun extractVcsErrors(exceptions: List<Exception>) = exceptions
		.filterIsInstance<VcsError>()
		.map { it.message }
		.filterNotNull()