package io.gitlab.arturbosch.quide.format.xml;

import java.time.Instant;

/**
 * @author Artur Bosch
 */
public interface Listing {

	String getTimestamp();

	default boolean isOlderThan(Instant instant) {
		Instant timeMillis = Instant.ofEpochMilli(Long.valueOf(getTimestamp()));
		return timeMillis.compareTo(instant) < 0 ;
	}
}
