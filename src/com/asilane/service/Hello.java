package com.asilane.service;

import com.asilane.recognition.Language;

/**
 * @author walane
 * 
 */
public class Hello implements Service {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String,
	 * com.asilane.recognition.Language)
	 */
	@Override
	public String handleService(final String sentence, final Language language) {
		if (language == Language.french) {
			return "Bonjour Thomas !";
		}

		return "Hello Thomas !";
	}
}
