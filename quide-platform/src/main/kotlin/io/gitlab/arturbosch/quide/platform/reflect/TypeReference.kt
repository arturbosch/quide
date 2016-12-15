package io.gitlab.arturbosch.quide.platform.reflect

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author Artur Bosch
 */
abstract class TypeReference<T> {

	var type: Type

	constructor() {
		val t = javaClass.genericSuperclass as? ParameterizedType ?:
				throw RuntimeException("Invalid TypeReference; must specify type parameters")

		if (t.rawType != TypeReference::class.java)
			throw RuntimeException("Invalid TypeReference; must directly extend TypeReference")

		type = t.actualTypeArguments[0]
	}

	constructor(type: Type) : this() {
		this.type = type
	}

	companion object {
		fun <T : Any> get(clazz: Class<T>) = SimpleTypeToken<T>(type = clazz)
		fun get(type: Type): SimpleTypeToken<*> = SimpleTypeToken<Any>(type)
	}

	override fun equals(other: Any?): Boolean {
		return other is TypeReference<*> && type == other.type
	}

	override fun hashCode(): Int {
		return type.hashCode()
	}
}

class SimpleTypeToken<T>(type: Type) : TypeReference<T>(type)