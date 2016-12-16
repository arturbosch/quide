package io.gitlab.arturbosch.quide.platform;

import io.gitlab.arturbosch.quide.detection.Detector;

import java.util.List;

/**
 * @author Artur Bosch
 */
public interface Plugin extends Nameable {

	Detector detector();

	List<Processor> processors();

	UserData userData();
}
