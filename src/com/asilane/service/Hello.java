package com.asilane.service;

import java.util.HashSet;
import java.util.Set;

import com.asilane.recognition.Language;

/**
 * @author walane
 * 
 */
public class Hello implements IService {

	private static final String GOODBYE = "good.*bye.*";
	private static final String SAY_GOOD_BYE_TO = "say good.*bye to.*";
	private static final String SAY_HELLO_TO = "say hello to.*";
	private static final String AU_REVOIR = "au revoir.*";
	private static final String DIS_BONJOUR_A = "di.* bonjour à.*";
	private static final String DIS_AU_REVOIR_A = "di.* au.*revoir à.*";
	private static final String HOW_ARE_YOU = "how are you";
	private static final String TU_VA_BIEN = "tu va.* bien";
	private static final String COMMENT_VAS_TU = "comment va.*tu";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Language)
	 */
	@Override
	public String handleService(final String sentence, final Language language) {
		if (language == Language.french) {
			if (sentence.matches(COMMENT_VAS_TU) || sentence.matches(TU_VA_BIEN)) {
				return "Je vais toujours bien.";
			} else if (sentence.matches(DIS_BONJOUR_A)) {
				return "Bonjour " + sentence.substring(sentence.lastIndexOf("à") + 1).trim() + ", comment allez-vous ?";
			} else if (sentence.matches(DIS_AU_REVOIR_A)) {
				return "Au revoir " + sentence.substring(sentence.lastIndexOf("à") + 1).trim() + " et à bientôt !";
			} else if (sentence.matches(AU_REVOIR)) {
				return "Au revoir et à bientôt !";
			}

			return "Bonjour !";
		}

		if (sentence.matches(HOW_ARE_YOU)) {
			return "I'm always good.";
		} else if (sentence.matches(SAY_HELLO_TO)) {
			return "Hello " + sentence.substring(sentence.lastIndexOf("to") + 2).trim() + ", how are you?";
		} else if (sentence.matches(SAY_GOOD_BYE_TO)) {
			return "Goodbye " + sentence.substring(sentence.lastIndexOf("to") + 2).trim() + ", see you soon.";
		} else if (sentence.matches(GOODBYE)) {
			return "Goodbye, see you soon.";
		}

		return "Hello !";
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
			set.add("bonjour");
			set.add("salut");
			set.add("hey");

			set.add(COMMENT_VAS_TU);
			set.add(TU_VA_BIEN);
			set.add(DIS_BONJOUR_A);
			set.add(DIS_AU_REVOIR_A);
			set.add(AU_REVOIR);
		} else {
			set.add("hello");
			set.add("hi");
			set.add("hey");

			set.add(HOW_ARE_YOU);
			set.add(SAY_HELLO_TO);
			set.add(SAY_GOOD_BYE_TO);
			set.add(GOODBYE);
		}

		return set;
	}
}