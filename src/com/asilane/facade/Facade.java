package com.asilane.facade;

import com.asilane.recognition.Command;
import com.asilane.recognition.Language;
import com.asilane.service.Service;

/**
 * @author walane
 * 
 *         Description : Intercept speech to text and call the good service
 */
public class Facade {

	/**
	 * @param sentence
	 * @param lang
	 * @return
	 */
	public static String handleSentence(final String sentence, final Language lang) {
		// Preparation of sentence
		final String preparedSentence = sentence.trim().toLowerCase();

		// Try to get the service corresponding to the sentence
		final Service askedService = Command.getInstance(lang).getService(preparedSentence);

		// Return the response of the service if this one is found
		if (askedService != null) {
			return askedService.handleService(preparedSentence, lang);
		}

		return "The sentence \"" + sentence + "\" not corresponding on any service in " + lang;
	}
}