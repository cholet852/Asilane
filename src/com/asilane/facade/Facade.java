package com.asilane.facade;

import com.asilane.recognition.Language;
import com.asilane.recognition.Translation;
import com.asilane.service.Hello;

/**
 * @author walane
 * 
 *         Description : Intercept speech to text and call the good service
 */
public class Facade {

	/**
	 * @param sentence
	 * @param language
	 * @return
	 */
	public static String handleSentence(final String sentence, final Language language) {
		// Preparation of sentence
		String preparedSentence = sentence.trim().toLowerCase();

		// Translate it if necessary
		if (language != Language.english) {
			preparedSentence = Translation.getInstance().getTranslation(preparedSentence, language);
		}

		// Call the good service
		if ("hello".equals(preparedSentence)) {
			return new Hello().handleService(preparedSentence, language);
		}

		return null;
	}
}