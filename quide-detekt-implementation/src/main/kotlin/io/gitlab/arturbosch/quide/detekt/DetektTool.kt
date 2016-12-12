package io.gitlab.arturbosch.quide.detekt

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.quide.detection.Detector
import io.gitlab.arturbosch.quide.model.SmellContainer
import io.gitlab.arturbosch.quide.vcs.Versionable

/**
 * @author Artur Bosch
 */
class DetektTool : Detector {

	override fun call(): SmellContainer<DetektCodeSmell> {
		return object : SmellContainer<DetektCodeSmell> {
			override fun all(): List<DetektCodeSmell> {
				throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
			}

			override fun findBySourcePath(path: String): List<DetektCodeSmell> {
				throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
			}

		}
	}

}

class DetektCodeSmell(id: String, entity: Entity) : CodeSmell(id, entity), io.gitlab.arturbosch.quide.model.CodeSmell {
	override fun startVersion(): Versionable {
		throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun setStartVersion(versionable: Versionable?) {
		throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun endVersion(): Versionable {
		throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun setEndVersion(versionable: Versionable?) {
		throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun isConsistent(): Boolean {
		throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun setConsistent(consistent: Boolean) {
		throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun weight(): Int {
		throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun addWeight(amount: Int) {
		throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun sourcePath(): String {
		throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}
