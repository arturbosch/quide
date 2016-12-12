package io.gitlab.arturbosch.quide.detection;

import io.gitlab.arturbosch.quide.model.SmellContainer;

import java.util.concurrent.Callable;

/**
 * @author Artur Bosch
 */
public interface Detector extends Tool, Callable<SmellContainer> {


}
