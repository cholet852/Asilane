package com.asilane.service;

import com.asilane.recognition.Language;

public interface Service {
	/**
	 * @param sentence
	 * @param language
	 * @return
	 */
	String handleService(final String sentence, final Language language);
}