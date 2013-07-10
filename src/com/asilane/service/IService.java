package com.asilane.service;

import java.util.Set;

import com.asilane.recognition.Language;

/**
 * @author walane
 * 
 */
public interface IService {
	/**
	 * This will return an appropriate answer to the question in the good language
	 * 
	 * @param sentence
	 * @param language
	 * @return an appropriate answer to the question in the good language
	 */
	String handleService(final String sentence, final Language language);

	/**
	 * This will returns all sentences which the service can be called
	 * 
	 * @param lang
	 * @return all sentences which the service can be called
	 */
	Set<String> getCommands(final Language lang);
}