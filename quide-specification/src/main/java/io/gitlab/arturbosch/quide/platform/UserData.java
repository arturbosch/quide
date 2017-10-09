package io.gitlab.arturbosch.quide.platform;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Artur Bosch
 */
public abstract class UserData implements AnalysisAware, ContainerAware, VersionAware {

	protected Map<String, Object> storage = new HashMap<>();

	@Override
	public Map<String, Object> storage() {
		return storage;
	}

}
