package io.gitlab.arturbosch.quide.platform.test

import io.gitlab.arturbosch.quide.platform.PluginDetector

/**
 * @author Artur Bosch
 */
class TestPluginDetector implements PluginDetector {
	@Override
	URL[] getJars() {
		return []
	}
}