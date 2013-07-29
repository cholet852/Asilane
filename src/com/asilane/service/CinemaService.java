package com.asilane.service;

import java.util.HashSet;
import java.util.Set;

import com.asilane.core.Language;
import com.asilane.core.facade.history.HistoryTree;

/**
 * @author walane
 * 
 */
public class CinemaService implements IService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Language)
	 */
	@Override
	public String handleService(final String sentence, final Language lang, final HistoryTree historyTree) {
		return "42";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#getCommands(com.asilane.recognition.Language)
	 */
	@Override
	public Set<String> getCommands(final Language lang) {
		final Set<String> set = new HashSet<String>();

		if (lang == Language.french) {
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
	 * @see com.asilane.service.IService#handleRecoveryService(java.lang.String, com.asilane.core.Language)
	 */
	@Override
	public String handleRecoveryService(final String sentence, final Language lang) {
		return null;
	}
}