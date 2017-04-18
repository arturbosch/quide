package io.gitlab.arturbosch.quide.crawler

import io.gitlab.arturbosch.kutils.notNullOrFalse
import io.gitlab.arturbosch.quide.crawler.cli.CLI
import io.gitlab.arturbosch.quide.crawler.cli.parse
import io.gitlab.arturbosch.quide.crawler.ui.RootView
import javafx.application.Application
import tornadofx.App
import tornadofx.View
import kotlin.reflect.KClass

/**
 * @author Artur Bosch
 */
class CrawlerApp : App() {
	override val primaryView: KClass<out View> = RootView::class
}

fun main(args: Array<String>) {
	with(args.parse()) {

		val ioNull = input == null && output == null

		if (ioNull && !withGUI.notNullOrFalse()) {
			println(usage)
			return
		}

		if (withGUI.notNullOrFalse()) {
			Application.launch(CrawlerApp::class.java, *args)
		} else {
			CLI.run(this)
		}

	}
}
