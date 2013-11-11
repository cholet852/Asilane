/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.core;

import java.util.Locale;

import com.asilane.core.facade.Facade;
import com.asilane.core.facade.NoServiceFoundException;
import com.asilane.core.facade.Question;
import com.asilane.core.facade.Response;

/**
 * Record voice, play and return the IA response
 * 
 * @author walane
 */
public class Asilane {
	private final Facade facade;
	private Locale lang;

	/**
	 * Create a new Asilane instance
	 */
	public Asilane(final EnvironmentTools environmentTools) {
		facade = new Facade(environmentTools);
		lang = Locale.ENGLISH;
	}

	/**
	 * Direct sentence handling without voice recognition
	 * 
	 * @param sentence
	 * @return The response of the IA corresponding to the sentence
	 */
	public Response handleSentence(final String sentence) {
		try {
			return facade.handleSentence(new Question(sentence, lang));
		} catch (final NoServiceFoundException e) {
			final Response response = new Response(e.getMessage());
			response.setError(true);

			return response;
		}
	}

	/**
	 * @return the lang
	 */
	public Locale getLocale() {
		return lang;
	}

	/**
	 * @param lang
	 *            the lang to set
	 */
	public void setLocale(final Locale lang) {
		this.lang = lang;
	}
}