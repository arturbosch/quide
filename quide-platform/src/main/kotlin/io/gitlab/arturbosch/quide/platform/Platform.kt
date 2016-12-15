package io.gitlab.arturbosch.quide.platform

/**
 * @author Artur Bosch
 */
fun main(args: Array<String>) {
	Platform().execute()
}

class Platform : ControlFlow {

	override fun state(): ControlFlow.State {
		return Plugin()
	}

}