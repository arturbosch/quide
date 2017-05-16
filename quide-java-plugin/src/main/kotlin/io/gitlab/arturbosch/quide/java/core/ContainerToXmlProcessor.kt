package io.gitlab.arturbosch.quide.java.core

import io.gitlab.arturbosch.quide.format.evolution.ContainerXmlParser
import io.gitlab.arturbosch.quide.java.format.JavaCodeSmellXmlParser
import io.gitlab.arturbosch.quide.java.safeContainer
import io.gitlab.arturbosch.quide.platform.ControlFlow
import io.gitlab.arturbosch.quide.platform.Processor
import io.gitlab.arturbosch.quide.platform.QuideConstants
import io.gitlab.arturbosch.quide.platform.UserData
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Artur Bosch
 */
open class ContainerToXmlProcessor : Processor {

	private val logger: Logger = LoggerFactory.getLogger(javaClass.simpleName)

	private val parser = ContainerXmlParser.create(JavaCodeSmellXmlParser())

	open fun ifPropertySet(data: UserData): Boolean = data.quideDirectory().getProperty(
			QuideConstants.VCS_OUTPUT_PER_VERSION)?.toBoolean() ?: false

	override fun <U : UserData> execute(data: U) {
		if (ifPropertySet(data)) {
			data.outputPath().ifPresent { output ->
				val currentVersion = data.currentVersion()
				val currentContainer = data.safeContainer()
				if (currentVersion.isPresent && currentContainer.isPresent) {
					val (version, container) = currentVersion.get() to currentContainer.get()
					val project = data.projectPath().fileName.toString()
					val file = output.resolve("$project.${version.versionNumber()}.xml").toFile()
					logger.info("Container saved to $file")
					parser.toXmlFile(file, version, container)
				}
			}
		}
	}

	override fun priority(): Int {
		return 10
	}

	override fun injectionPoint(): ControlFlow.InjectionPoint {
		return io.gitlab.arturbosch.quide.platform.ControlFlow.InjectionPoint.AfterDetection
	}
}
