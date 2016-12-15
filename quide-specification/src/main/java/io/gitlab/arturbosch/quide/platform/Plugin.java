package io.gitlab.arturbosch.quide.platform;

import io.gitlab.arturbosch.quide.detection.Detector;
import io.gitlab.arturbosch.quide.mapping.SmellMapping;

import java.util.List;

/**
 * @author Artur Bosch
 */
public interface Plugin extends Nameable {

	Detector detector();

	List<Processor> processors();

	SmellMapping mapping();

	UserData userData();
}
