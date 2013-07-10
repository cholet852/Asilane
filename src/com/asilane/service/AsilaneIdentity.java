package com.asilane.service;

import java.util.HashSet;
import java.util.Set;

import com.asilane.recognition.Language;

/**
 * @author walane
 * 
 */
public class AsilaneIdentity implements IService {

	private static final String WHAT_IS_YOUR_GOAL = "what is your goal";
	private static final String QUELLE_EST_TA_MISSION = "quel.* est ta mission";
	private static final String QUEL_EST_TON_BUT = "quel.* est ton but";
	private static final String ASILANE = "asilane";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleService(java.lang.String, com.asilane.recognition.Language)
	 */
	@Override
	public String handleService(final String sentence, final Language language) {
		if (language == Language.french) {
			if (sentence.matches(ASILANE)) {
				return "Oui ?";
			}
			if (sentence.matches(QUEL_EST_TON_BUT) || sentence.matches(QUELLE_EST_TA_MISSION)) {
				return "Je suis là pour vous aider, c'est Walane qui m'a appris comment faire.";
			}

			return "Je suis Asilane, un assistant intelligent, j'ai été créé par Walane.";
		}

		if (sentence.matches(ASILANE)) {
			return "Yes ?";
		}
		if (sentence.matches(WHAT_IS_YOUR_GOAL)) {
			return "I am here to help you, Walane tolds me how to do.";
		}

		return "I am Asilane, an intelligent assistant, i've been build by Walane.";
	}

	@Override
	public Set<String> getCommands(final Language lang) {
		final Set<String> set = new HashSet<String>();

		if (lang == Language.french) {
			set.add("qui es-tu.*");
			set.add("qui tu es");
			set.add(QUEL_EST_TON_BUT);
			set.add(QUELLE_EST_TA_MISSION);
			set.add(ASILANE);
		} else {
			set.add("who are you");
			set.add(ASILANE);
		}

		return set;
	}
}