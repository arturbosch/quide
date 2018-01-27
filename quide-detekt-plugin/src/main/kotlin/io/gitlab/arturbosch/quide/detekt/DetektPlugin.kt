package io.gitlab.arturbosch.quide.detekt

import io.gitlab.arturbosch.quide.api.Plugin

/**
 * @author Artur Bosch
 */
class DetektPlugin : Plugin {

	override fun define(context: Plugin.Context) {
		context.register(DetektTool())
	}
}
