package io.gitlab.arturbosch.quide.crawler

import io.gitlab.arturbosch.quide.crawler.ui.ContentView
import io.gitlab.arturbosch.quide.crawler.ui.MenuView
import io.gitlab.arturbosch.quide.crawler.ui.ProgressView
import javafx.application.Application
import tornadofx.App
import tornadofx.View
import tornadofx.borderpane
import kotlin.reflect.KClass

/**
 * @author Artur Bosch
 */
class Viewer : App() {
	override val primaryView: KClass<out View> = RootView::class
}

fun main(args: Array<String>) {
	Application.launch(Viewer::class.java, *args)
}

class RootView : View("") {

	override val root: javafx.scene.Parent = borderpane {
		prefWidth = 1200.0
		prefHeight = 800.0
		top = find<MenuView>().root
		bottom = find<ProgressView>().root
		center = find<ContentView>().root
	}

}