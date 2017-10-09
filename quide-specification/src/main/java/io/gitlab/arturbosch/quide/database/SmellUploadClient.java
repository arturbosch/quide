package io.gitlab.arturbosch.quide.database;

import io.gitlab.arturbosch.quide.model.SmellContainer;

/**
 * @author Artur Bosch
 */
public interface SmellUploadClient {

	void upload(SmellContainer<?> smellContainer);
}
