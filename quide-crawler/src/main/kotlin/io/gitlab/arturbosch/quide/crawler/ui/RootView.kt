package io.gitlab.arturbosch.quide.crawler.ui

import tornadofx.View
import tornadofx.borderpane

/**
 * @author Artur Bosch
 */
class RootView : View("") {

	override val root: javafx.scene.Parent = borderpane {
		prefWidth = 1200.0
		prefHeight = 800.0
		top = find<MenuView>().root
		bottom = find<ProgressView>().root
		center = find<ContentView>().root
	}

}