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

	private final Set<String> commands = new HashSet<String>();

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
		if (commands.isEmpty()) {

			if (lang == Locale.FRANCE) {
				commands.add("quel.* est le sens de la vie");
				commands.add("quel.* est la réponse à l'univers");
				commands.add("quel.* est le nombre ultime");
			} else {
				commands.add("what is the answer of the universe");
				commands.add("what is the ultimate number");
			}
		}

		return commands;
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