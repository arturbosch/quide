package io.gitlab.arturbosch.quide.core

import io.gitlab.arturbosch.quide.api.core.StorageAware
import java.util.HashMap

/**
 * @author Artur Bosch
 */
open class Storage(private val storage: HashMap<String, Any> = HashMap()) : StorageAware {

	@Suppress("UNCHECKED_CAST")
	override operator fun <T> get(key: String): T? = storage[key] as? T?

	override fun <T : Any> put(key: String, value: T) {
		storage[key] = value
	}

}
