package com.asilane.core.facade;

import java.util.Collection;
import java.util.Properties;

/**
 * Represent a translation
 * 
 * @author walane
 * 
 */
public class Translation {
	private final Properties translationMap;
	private static final String ANSWER = "_answer";

	/**
	 * @param translationMap
	 */
	protected Translation(final Properties translationMap) {
		this.translationMap = translationMap;
	}

	/**
	 * @param key
	 * @return The question corresponding to the translation key
	 */
	public String getQuestion(final String key) {
		return String.valueOf(translationMap.get(key));
	}

	/**
	 * @param key
	 * @return The answer corresponding to the translation key
	 */
	public String getAnswer(final String key) {
		return String.valueOf(translationMap.get(key + ANSWER));
	}

	/**
	 * @see Properties.values()
	 */
	public Collection<Object> values() {
		return translationMap.values();
	}
}