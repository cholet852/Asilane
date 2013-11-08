package com.asilane.core;

import java.util.regex.Matcher;

/**
 * @author walane
 * 
 */
public class RegexVarsResult {

	/**
	 * The RegexVarsResult class is just a simple tool to access to hide matcher methods
	 */
	private final Matcher matcher;

	/**
	 * Create a new RegexVarsResult instance
	 * 
	 * @param matcher
	 */
	public RegexVarsResult(final Matcher matcher) {
		this.matcher = matcher;
	}

	/**
	 * Get the named regex value by his key<br>
	 * For example : "what the weather like in (?<city> (.*))
	 * 
	 * @param key
	 * @return the named regex value by his key
	 */
	public String get(final String key) {
		return matcher.group(key).trim();
	}

	/**
	 * Get the non-named regex value by his number<br>
	 * For example : "what .* is .* a .*"
	 * 
	 * @param number
	 * @return the non-named regex value by his number
	 */
	public String get(final int number) {
		return matcher.group(number).trim();
	}

	/**
	 * @return the matcher
	 */
	public Matcher getMatcher() {
		return matcher;
	}
}