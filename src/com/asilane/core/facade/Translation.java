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
	 * 
	 * @param key
	 * @param vars
	 * @return The answer corresponding to the translation key<br>
	 *         Example : <code>getAnswer("prety_cloudy", "Paris")</code> :<br>
	 *         "It's pretty cloudy in {0}" -> "It's pretty cloudy in Paris"
	 */
	public String getAnswer(final String key, final String... vars) {
		String response = String.valueOf(translationMap.get(key + ANSWER));

		// Parse {n}
		for (int i = 0; i < vars.length; i++) {
			response = response.replace("{" + i + "}", vars[i]);
		}

		return response;
	}

	/**
	 * @see Properties.values()
	 */
	public Collection<Object> values() {
		return translationMap.values();
	}
}