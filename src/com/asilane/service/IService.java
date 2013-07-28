package com.asilane.service;

import java.util.Set;

import com.asilane.core.Language;

/**
 * Each Service have to implemente this interface to be used on the application
 * 
 * @author walane
 */
public interface IService {
	/**
	 * This will return an appropriate answer to the question in the good language
	 * 
	 * @param sentence
	 * @param language
	 * @return an appropriate answer to the question in the good language
	 */
	String handleService(final String sentence, final Language lang);

	/**
	 * This will returns all sentences which the service can be called
	 * 
	 * @param lang
	 * @return all sentences which the service can be called
	 */
	Set<String> getCommands(final Language lang);

	/**
	 * This will return an appropriate answer if there is no answer given
	 * 
	 * @param sentence
	 * @param language
	 * @return an appropriate answer if there is no answer given
	 */
	String handleRecoveryService(final String sentence, final Language lang);
}