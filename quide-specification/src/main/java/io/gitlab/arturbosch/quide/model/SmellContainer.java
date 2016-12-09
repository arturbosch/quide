package io.gitlab.arturbosch.quide.model;

import java.util.List;

/**
 * Represents a container for smells.
 *
 * @author Artur Bosch
 */
public interface SmellContainer {

	/**
	 * @return a list of all containing smells
	 */
	List<CodeSmell> all();

	/**
	 * @param path the path used as a filter
	 * @return a subset of smells for given path
	 */
	List<CodeSmell> findBySourcePath(String path);
}
