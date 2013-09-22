package com.asilane.service;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.asilane.core.facade.history.HistoryTree;

/**
 * @author walane
 * 
 */
public class FortyTwoService implements IService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Locale)
	 */
	@Override
	public String handleService(final String sentence, final Locale lang, final HistoryTree historyTree) {
		return "42";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#getCommands(com.asilane.recognition.Locale)
	 */
	@Override
	public Set<String> getCommands(final Locale lang) {
		final Set<String> set = new HashSet<String>();

		if (lang == Locale.FRANCE) {
			set.add("quel.* est le sens de la vie");
			set.add("quel.* est la réponse à l'univers");
			set.add("quel.* est le nombre ultime");
		} else {
			set.add("what is the answer of the universe");
			set.add("what is the ultimate number");
		}

		return set;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleRecoveryService(java.lang.String, com.asilane.core.Locale)
	 */
	@Override
	public String handleRecoveryService(final String sentence, final Locale lang, final HistoryTree historyTree) {
		return null;
	}
}