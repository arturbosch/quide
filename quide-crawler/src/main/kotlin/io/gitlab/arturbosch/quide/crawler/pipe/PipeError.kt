package io.gitlab.arturbosch.quide.crawler.pipe

/**
 * @author Artur Bosch
 */
data class PipeError(override val message: String?) : RuntimeException(message)