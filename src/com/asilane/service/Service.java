package com.asilane.service;

import java.util.Set;

import com.asilane.recognition.Language;

public interface Service {
	/**
	 * @param sentence
	 * @param language
	 * @return
	 */
	String handleService(final String sentence, final Language language);

	/**
	 * @param lang
	 * @return
	 */
	Set<String> getCommands(final Language lang);
}