package io.gitlab.arturbosch.quide.platform;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

/**
 * @author Artur Bosch
 */
public interface ControlFlow {

	Logger LOGGER = LogManager.getLogger(ControlFlow.class.getSimpleName());

	enum InjectionPoint {
		BeforeDetection, AfterDetection, AfterAnalysis
	}

	List<Plugin> plugins();

	default void execute(Path projectPath) {
		LOGGER.info("Starting quide ...");
		plugins().forEach(plugin -> {
			String toolName = plugin.name();
			LOGGER.info("Starting '" + toolName + "' ...");
			UserData data = plugin.userData();
			data.put(UserData.PROJECT_PATH, projectPath);
			List<Processor> processors = plugin.processors();
			executeProcessors(processors, data, InjectionPoint.BeforeDetection);
			String detectorName = plugin.detector().name();
			LOGGER.info("Starting '" + detectorName + "' ...");
			plugin.detector().execute(data);
			executeProcessors(processors, data, InjectionPoint.AfterDetection);
			executeProcessors(processors, data, InjectionPoint.AfterAnalysis);
		});
	}

	default void executeProcessors(List<Processor> processors,
								   UserData userData,
								   InjectionPoint injectionPoint) {
		processors.stream()
				.filter(processor -> processor.injectionPoint().equals(injectionPoint))
				.sorted(Comparator.comparingInt(Processor::priority))
				.forEach(processor -> {
					String name = processor.name();
					LOGGER.info("Executing processor '" + name + "' (InjectionPoint: " + injectionPoint + ")");
					processor.execute(userData);
				});
	}

}
