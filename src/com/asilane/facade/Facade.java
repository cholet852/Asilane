package com.asilane.facade;

import com.asilane.recognition.Language;
import com.asilane.service.IService;

/**
 * @author walane
 */
public class Facade {

	/**
	 * Intercept speech to text, prepare the sentence and call the good service
	 * 
	 * @param sentence
	 * @param lang
	 * @return the response
	 */
	public static String handleSentence(final String sentence, final Language lang) {
		// Preparation of sentence
		final String preparedSentence = sentence.trim().toLowerCase();

		// Try to get the service corresponding to the sentence
		final IService askedService = ServiceDispatcher.getInstance(lang).getService(preparedSentence);

		// Return the response of the service if this one is found
		if (askedService != null) {
			return askedService.handleService(preparedSentence, lang);
		}

		// No any command valid, error message
		if (lang == Language.french) {
			return "Je n'ai pas bien compris, pouvez-vous répéter ?";
		}
		return "I don't understand, can you repeat?";
	}
}