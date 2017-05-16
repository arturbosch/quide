package io.gitlab.arturbosch.quide.java.core

import io.gitlab.arturbosch.quide.platform.ControlFlow
import io.gitlab.arturbosch.quide.platform.QuideConstants
import io.gitlab.arturbosch.quide.platform.UserData

/**
 * @author Artur Bosch
 */
class AfterAnalysisContainerToXmlProcessor : ContainerToXmlProcessor() {

	override fun ifPropertySet(data: UserData): Boolean = data.quideDirectory().getProperty(
			QuideConstants.VCS_OUTPUT)?.toBoolean() ?: false

	override fun injectionPoint(): ControlFlow.InjectionPoint {
		return ControlFlow.InjectionPoint.AfterAnalysis
	}
}