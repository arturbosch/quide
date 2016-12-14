package io.gitlab.arturbosch.quide.model;

import java.util.List;

/**
 * Represents a container for smells.
 *
 * @author Artur Bosch
 */
public interface SmellContainer<T extends CodeSmell> {

	/**
	 * @return a list of all containing smells
	 */
	List<T> all();

	/**
	 * @param path the path used as a filter
	 * @return a subset of smells for given path
	 */
	List<T> findBySourcePath(String path);
}
