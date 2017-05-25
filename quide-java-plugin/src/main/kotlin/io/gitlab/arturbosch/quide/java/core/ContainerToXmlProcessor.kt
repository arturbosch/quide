package io.gitlab.arturbosch.quide.java.core

import io.gitlab.arturbosch.quide.format.evolution.ContainerXmlParser
import io.gitlab.arturbosch.quide.java.format.JavaCodeSmellXmlParser
import io.gitlab.arturbosch.quide.java.withOutputPath
import io.gitlab.arturbosch.quide.platform.ControlFlow
import io.gitlab.arturbosch.quide.platform.QuideConstants
import io.gitlab.arturbosch.quide.platform.UserData
import io.gitlab.arturbosch.quide.platform.processors.ConditionalProcessor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Artur Bosch
 */
open class ContainerToXmlProcessor : ConditionalProcessor {

	private val logger: Logger = LoggerFactory.getLogger(javaClass.simpleName)
	private val parser = ContainerXmlParser.create(JavaCodeSmellXmlParser())

	override fun <U : UserData> isActive(data: U): Boolean = data.quideDirectory().getProperty(
			QuideConstants.VCS_OUTPUT_PER_VERSION)?.toBoolean() ?: false

	override fun <U : UserData> doIfActive(data: U) {
		data.withOutputPath { output, version, container ->
			val project = data.projectPath().fileName.toString()
			val file = output.resolve("$project.${version.versionNumber()}.xml").toFile()
			logger.info("Container saved to $file")
			parser.toXmlFile(file, version, container)
		}
	}

	override fun priority(): Int = 10

	override fun injectionPoint(): ControlFlow.InjectionPoint {
		return ControlFlow.InjectionPoint.AfterDetection
	}
}
