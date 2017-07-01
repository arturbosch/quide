package io.gitlab.arturbosch.quide.api.core

import java.util.HashMap

/**
 * @author Artur Bosch
 */
interface StorageAware {
	operator fun <T> get(key: String): T?
	fun <T : Any> put(key: String, value: T)
}

abstract class Storage : StorageAware {

	protected val storage: HashMap<String, Any> = HashMap()

	@Suppress("UNCHECKED_CAST")
	override operator fun <T> get(key: String): T? {
		return storage[key] as T?
	}

	override fun <T : Any> put(key: String, value: T) {
		storage.put(key, value)
	}

}