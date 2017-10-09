package io.gitlab.arturbosch.quide.api.processors

/**
 * Specifies when to run a processor in the analysis lifecycle.
 *
 * @author Artur Bosch
 */
enum class InjectionPoint {
	BeforeDetection, AfterDetection, BeforeAnalysis, AfterAnalysis
}