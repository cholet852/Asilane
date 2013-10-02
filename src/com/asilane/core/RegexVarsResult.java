package com.asilane.core;

import java.util.List;
import java.util.Map;

/**
 * @author walane
 * 
 */
public class RegexVarsResult {

	/**
	 * For each regex name, his value<br>
	 * For example : "what the weather like in (city .*) ?" -> (city -> .*)
	 */
	private final Map<String, String> namedRegex;

	/**
	 * Represent the non-named regex<br>
	 * For example : "what .* is .* a .*" -> (List)<br>
	 * In this case : "what .* is (city .*) and .*" this return the two non-named regex
	 */
	private final List<String> otherRegex;

	/**
	 * Create a new RegexVarsResult instance
	 * 
	 * @param namedRegex
	 *            For each regex name, his value<br>
	 *            For example : "what the weather like in (city .*) ?" -> (city -> .*)
	 * @param otherRegex
	 *            Represent the non-named regex<br>
	 *            For example : "what .* is .* a .*" -> (List)<br>
	 *            In this case : "what .* is (city .*) and .*" this return the two non-named regex
	 */
	public RegexVarsResult(final Map<String, String> namedRegex, final List<String> otherRegex) {
		this.namedRegex = namedRegex;
		this.otherRegex = otherRegex;
	}

	/**
	 * @return the namedRegex :<br>
	 *         For each regex name, his value<br>
	 *         For example : "what the weather like in (city .*) ?" -> (city -> .*)
	 */
	public Map<String, String> getNamedRegex() {
		return namedRegex;
	}

	/**
	 * @return the otherRegex :<br>
	 *         Represent the non-named regex<br>
	 *         For example : "what .* is .* a .*" -> (List)<br>
	 *         In this case : "what .* is (city .*) and .*" this return the two non-named regex
	 */
	public List<String> getOtherRegex() {
		return otherRegex;
	}
}