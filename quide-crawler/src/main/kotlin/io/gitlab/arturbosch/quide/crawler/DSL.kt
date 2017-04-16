package io.gitlab.arturbosch.quide.crawler

val config = configuration {
	name = "MyConfiguration"
	strings {
		string("New")
		string("Old")
	}
}

data class Config(var name: String = "", var names: MutableList<String> = mutableListOf()) {
	fun strings(function: Liste.() -> Unit) {
		val liste = Liste()
		function.invoke(liste)
		names = liste.list
	}

}

data class Liste(val list: MutableList<String> = mutableListOf()) {
	fun string(s: String) {
		list.add(s)
	}
}


fun configuration(dsl: Config.() -> Unit): Config {
	val config = Config()
	dsl.invoke(config)
	return config
}

fun main(args: Array<String>) {
	println(config)
}