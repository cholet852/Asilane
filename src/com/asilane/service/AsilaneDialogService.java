package com.asilane.service;

import java.util.HashSet;
import java.util.Set;

import com.asilane.core.Language;
import com.asilane.facade.history.HistoryTree;

/**
 * @author walane
 * 
 */
public class AsilaneDialogService implements IService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Language)
	 */
	@Override
	public String handleService(final String sentence, final Language lang, final HistoryTree historyTree) {
		if (sentence.matches("oui")) {
			return "Ok.";
		} else if (sentence.matches("non")) {
			return "Pourquoi non ?";
		} else if (sentence.matches(".*merci")) {
			return "Derien, c'est un plaisir de vous aider.";
		}

		return "Ok.";
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
			set.add("oui");
			set.add("non");
			set.add(".*merci");
		} else {
			set.add("yes");
			set.add("no");
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