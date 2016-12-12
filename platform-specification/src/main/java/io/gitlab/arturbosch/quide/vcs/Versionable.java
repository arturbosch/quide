package io.gitlab.arturbosch.quide.vcs;

import java.util.List;

/**
 * Represents a version.
 *
 * @author Artur Bosch
 */
public interface Versionable extends Comparable<Versionable> {

	int versionNumber();

	default String versionHash() {
		return revision().versionHash();
	}

	default String parentHash() {
		return revision().parentHash();
	}

	Revision revision();

	List<FileChange> fileChanges();

	@Override
	default int compareTo(Versionable other) {
		return Integer.compare(versionNumber(), other.versionNumber());
	}
}
