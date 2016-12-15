package io.gitlab.arturbosch.quide.platform;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Artur Bosch
 */
public interface ControlFlow {

	Logger LOGGER = Logger.getLogger(ControlFlow.class.getSimpleName());

	enum InjectionPoint {
		BeforeDetection, AfterDetection, AfterAnalysis
	}

	List<Plugin> plugins();

	default void execute(Path projectPath) {
		LOGGER.info("Starting quide...");
		plugins().forEach(plugin -> {
			LOGGER.info("Running " + plugin.detector().name() + "...");
			UserData data = plugin.userData();
			data.put(UserData.PROJECT_PATH, projectPath);
			List<Processor> processors = plugin.processors();
			executeProcessors(processors, data, InjectionPoint.BeforeDetection);
			plugin.detector().execute(data);
			executeProcessors(processors, data, InjectionPoint.AfterDetection);
			plugin.mapping().execute(data);
			executeProcessors(processors, data, InjectionPoint.AfterAnalysis);
		});
	}

	default void executeProcessors(List<Processor> processors,
								   UserData userData,
								   InjectionPoint injectionPoint) {
		processors.stream()
				.filter(processor -> processor.injectionPoint().equals(injectionPoint))
				.sorted(Comparator.comparingInt(Processor::priority))
				.forEach(processor -> processor.execute(userData));
	}

}
