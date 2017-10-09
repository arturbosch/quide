package io.gitlab.arturbosch.quide.format.xml;

import io.gitlab.arturbosch.quide.format.internal.Validate;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;

/**
 * @author Artur Bosch
 */
@XmlRootElement(name = "Blacklist")
public class Blacklist implements Listing {

	@XmlElement(name = "ID")
	private List<String> ids;
	@XmlAttribute(name = "timestamp")
	private String timestamp;

	Blacklist() {
	}

	public Blacklist(List<String> ids, String timestamp) {
		this.ids = ids;
		this.timestamp = timestamp;
	}

	public static Blacklist withNewTimestamp(String timestamp, Blacklist blacklist) {
		Validate.notNull(timestamp);
		Validate.notNull(blacklist);
		return new Blacklist(blacklist.ids, timestamp);
	}

	public List<String> getIds() {
		return ids == null ? ids = Collections.emptyList() : ids;
	}

	@Override
	public String getTimestamp() {
		return timestamp;
	}

	@Override
	public String toString() {
		return "Blacklist{" +
				"ids=" + ids +
				", timestamp='" + timestamp + '\'' +
				'}';
	}
}
