package io.gitlab.arturbosch.quide.platform;

import io.gitlab.arturbosch.quide.model.SmellContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

/**
 * @author Artur Bosch
 */
public interface ControlFlow {

	Logger LOGGER = LoggerFactory.getLogger(ControlFlow.class.getSimpleName());

	enum InjectionPoint {
		BeforeDetection, AfterDetection, BeforeAnalysis, AfterAnalysis
	}

	List<Plugin> plugins();

	default void execute(Plugin plugin, Path input, Path output) {
		UserData data = plugin.userData();
		String detectorName = plugin.detector().name();

		data.put(UserData.TOOL_NAME, detectorName);
		data.put(UserData.PROJECT_PATH, input);
		data.put(UserData.OUTPUT_PATH, output);

		List<Processor> processors = plugin.processors();
		executeProcessors(processors, data, InjectionPoint.BeforeAnalysis);
		executeProcessors(processors, data, InjectionPoint.BeforeDetection);

		LOGGER.info("Starting '" + detectorName + "' ...");

		data.currentContainer().ifPresent(currentContainer ->
				data.put(UserData.LAST_CONTAINER, currentContainer));
		SmellContainer container = plugin.detector().execute(data);
		data.put(UserData.CURRENT_CONTAINER, container);

		executeProcessors(processors, data, InjectionPoint.AfterDetection);
		executeProcessors(processors, data, InjectionPoint.AfterAnalysis);
	}

	default void executeProcessors(List<Processor> processors,
								   UserData userData,
								   InjectionPoint injectionPoint) {
		processors.stream()
				.filter(processor -> processor.injectionPoint().equals(injectionPoint))
				.sorted(Comparator.comparingInt(Processor::priority))
				.forEach(processor -> {
					String name = processor.name();
					LOGGER.info("Executing processor '" + name + "'");
					processor.execute(userData);
				});
	}

}
