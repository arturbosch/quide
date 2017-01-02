package io.gitlab.arturbosch.quide.format.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Artur Bosch
 */
@XmlRootElement(name = "SmellBaseline")
public class SmellBaseline {

	@XmlElement(name = "Blacklist", required = true)
	private Blacklist blacklist;
	@XmlElement(name = "Whitelist", required = true)
	private Whitelist whitelist;

	SmellBaseline() {
	}

	public SmellBaseline(Blacklist blacklist, Whitelist whitelist) {
		this.blacklist = blacklist;
		this.whitelist = whitelist;
	}

	public Blacklist getBlacklist() {
		return blacklist;
	}

	public Whitelist getWhitelist() {
		return whitelist;
	}

	@Override
	public String toString() {
		return "SmellBaseline{" +
				"blacklist=" + blacklist +
				", whitelist=" + whitelist +
				'}';
	}
}
