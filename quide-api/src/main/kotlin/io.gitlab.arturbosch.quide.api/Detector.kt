package io.gitlab.arturbosch.quide.api

import io.gitlab.arturbosch.quide.model.SmellContainer
import io.gitlab.arturbosch.quide.platform.Nameable

/**
 * @author Artur Bosch
 */
interface Detector<out S : SmellContainer<*>> : Executable<S>, Nameable
