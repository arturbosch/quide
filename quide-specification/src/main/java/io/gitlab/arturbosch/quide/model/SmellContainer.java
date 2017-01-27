package io.gitlab.arturbosch.quide.model;

import java.util.List;
import java.util.stream.Collectors;

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
	default List<T> findBySourcePath(String path) {
		return all().stream()
				.filter(smell -> smell.sourcePath().equals(path))
				.collect(Collectors.toList());
	}

	/**
	 * Size of the smell container eg. #smells.
	 *
	 * @return amount of smells
	 */
	default int size() {
		return all().size();
	}

	/**
	 * Filters the container for living smells/not killed within an evolutionary analysis.
	 *
	 * @return living smells
	 */
	default List<T> alive() {
		return all().stream()
				.filter(CodeSmell::isAlive)
				.collect(Collectors.toList());
	}

	/**
	 * Combination of {@link #findBySourcePath(String)} and {@link #alive()}
	 *
	 * @param path path smells need to have
	 * @return smells in given path and which are alive
	 */
	default List<T> aliveInPath(String path) {
		return all().stream()
				.filter(smell -> smell.sourcePath().equals(path))
				.filter(CodeSmell::isAlive)
				.collect(Collectors.toList());
	}
}
