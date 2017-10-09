package io.gitlab.arturbosch.quide.vcs;

import java.time.ZonedDateTime;

/**
 * @author Artur Bosch
 */
public interface Revision {

	String versionHash();

	String parentHash();

	String message();

	String author();

	ZonedDateTime date();

	boolean isMerge();

}
