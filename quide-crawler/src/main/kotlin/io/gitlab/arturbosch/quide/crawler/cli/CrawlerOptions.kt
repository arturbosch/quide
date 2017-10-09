package io.gitlab.arturbosch.quide.crawler.cli

/**
 * @author Artur Bosch
 */
data class CrawlerOptions(private val operations: List<String>) {
	fun containsOperations(operation: String) = operations.contains(operation)
}