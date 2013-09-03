package com.asilane.service;

import java.util.Locale;
import java.util.Set;

import com.asilane.core.facade.history.HistoryTree;

/**
 * Each Service have to implemente this interface to be used on the application
 * 
 * @author walane
 */
public interface IService {
	/**
	 * This will return an appropriate answer to the question in the good Locale
	 * 
	 * @param sentence
	 * @param historyTree
	 * @param Locale
	 * @return an appropriate answer to the question in the good Locale
	 */
	String handleService(final String sentence, final Locale lang, final HistoryTree historyTree);

	/**
	 * This will returns all sentences which the service can be called
	 * 
	 * @param lang
	 * @return all sentences which the service can be called
	 */
	Set<String> getCommands(final Locale lang);

	/**
	 * This will return an appropriate answer if there is no answer given
	 * 
	 * @param sentence
	 * @param Locale
	 * @return an appropriate answer if there is no answer given
	 */
	String handleRecoveryService(final String sentence, final Locale lang);
}