package io.gitlab.arturbosch.quide.platform.processors;

import io.gitlab.arturbosch.quide.model.Printable;
import io.gitlab.arturbosch.quide.platform.ControlFlow;
import io.gitlab.arturbosch.quide.platform.Processor;
import io.gitlab.arturbosch.quide.platform.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

/**
 * @author Artur Bosch
 */
public class ResultConsoleProcessor implements Processor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResultConsoleProcessor.class.getSimpleName());

	@Override
	public <U extends UserData> void execute(U u) {
		u.currentContainer().ifPresent(container -> {
			String result = container.alive().stream()
					.map(Printable::asPrintable)
					.collect(Collectors.joining("\n"));
			LOGGER.info("Analysis result of " + u.toolName() + ": \n\n" + result + "\n");
		});
	}

	@Override
	public ControlFlow.InjectionPoint injectionPoint() {
		return ControlFlow.InjectionPoint.AfterAnalysis;
	}
}
