package io.gitlab.arturbosch.quide.platform.processors;

import io.gitlab.arturbosch.quide.platform.ControlFlow;
import io.gitlab.arturbosch.quide.platform.Processor;
import io.gitlab.arturbosch.quide.platform.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Artur Bosch
 */
public class NumberOfSmellsProcessor implements Processor {

	private static final Logger LOGGER = LoggerFactory.getLogger(NumberOfSmellsProcessor.class.getSimpleName());

	@Override
	public <U extends UserData> void execute(U u) {
		u.currentContainer().ifPresent(container -> {
			int size = container.all().size();
			LOGGER.info(u.toolName() + " detected: #" + size + " code smells");
		});
	}

	@Override
	public ControlFlow.InjectionPoint injectionPoint() {
		return ControlFlow.InjectionPoint.AfterAnalysis;
	}
}
