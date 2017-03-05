package io.gitlab.arturbosch.quide.platform

/**
 * @author Artur Bosch
 */
object HomeFolder : QuideDirectory.DefaultQuideDirectory()

fun String.asProperty(): String? = HomeFolder.getProperty(this)