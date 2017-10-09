package io.gitlab.arturbosch.quide.shell

/**
 * @author Artur Bosch
 */
class QuideShellException(override val message: String? = null,
						  override val cause: Throwable? = null) : IllegalStateException(message, cause)