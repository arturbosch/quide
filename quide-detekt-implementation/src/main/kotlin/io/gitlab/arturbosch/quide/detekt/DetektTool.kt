package io.gitlab.arturbosch.quide.detekt

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.quide.detection.Detector
import io.gitlab.arturbosch.quide.model.BaseCodeSmell
import io.gitlab.arturbosch.quide.model.SmellContainer
import io.gitlab.arturbosch.quide.platform.UserData

/**
 * @author Artur Bosch
 */
class DetektTool : Detector<DetektCodeSmell> {

	override fun run(userData: UserData): SmellContainer<DetektCodeSmell> {
		return object : SmellContainer<DetektCodeSmell> {
			override fun all(): MutableList<DetektCodeSmell> {
				throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
			}

			override fun findBySourcePath(path: String?): MutableList<DetektCodeSmell> {
				throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
			}

		}
	}

}

class DetektCodeSmell(private val smell: CodeSmell) : BaseCodeSmell()
