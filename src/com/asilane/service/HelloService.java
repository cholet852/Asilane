package com.asilane.service;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.asilane.core.AsilaneUtils;
import com.asilane.core.facade.history.HistoryTree;

/**
 * @author walane
 * 
 */
public class HelloService implements IService {

	private final Set<String> commands = new HashSet<String>();

	private static final String GOODBYE = "good.*bye.*";
	private static final String SAY_GOOD_BYE_TO = "say good.*bye to.*";
	private static final String SAY_HELLO_TO = "say hello to.*";

	private static final String AU_REVOIR = "au revoir.*";
	private static final String DIS_BONJOUR_A = "di.* bonjour à.*";
	private static final String DIS_AU_REVOIR_A = "di.* au.*revoir à.*";
	private static final String HOW_ARE_YOU = "how are you";
	private static final String TU_VA_BIEN = ".*tu va.* bien";
	private static final String CA_VA = ".a va";
	private static final String COMMENT_CA_VA = ".*comment .. va.*";
	private static final String COMMENT_VAS_TU = ".*comment va.*tu";
	private static final String COMMENT_ALLEZ_VOUS = ".*comment allez.*vous";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Locale)
	 */
	@Override
	public String handleService(final String sentence, final Locale lang, final HistoryTree historyTree) {
		List<String> regexVars = null;

		if (lang == Locale.FRANCE) {
			if (sentence.matches(CA_VA) || sentence.matches(COMMENT_CA_VA) || sentence.matches(COMMENT_VAS_TU)
					|| sentence.matches(TU_VA_BIEN) || sentence.matches(COMMENT_ALLEZ_VOUS)) {
				return "Je vais toujours bien.";
			} else if ((regexVars = AsilaneUtils.extractRegexVars(DIS_BONJOUR_A, sentence)) != null) {
				return "Bonjour " + regexVars.get(1) + ", comment allez-vous ?";
			} else if ((regexVars = AsilaneUtils.extractRegexVars(DIS_AU_REVOIR_A, sentence)) != null) {
				return "Au revoir " + regexVars.get(2) + " et à bientôt !";
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
	 * @see com.asilane.service.IService#getCommands(com.asilane.recognition.Locale)
	 */
	@Override
	public Set<String> getCommands(final Locale lang) {
		if (commands.isEmpty()) {
			if (lang == Locale.FRANCE) {
				commands.add("bonjour");
				commands.add("salut");
				commands.add("hey");

				commands.add(CA_VA);
				commands.add(COMMENT_CA_VA);
				commands.add(COMMENT_VAS_TU);
				commands.add(COMMENT_ALLEZ_VOUS);
				commands.add(TU_VA_BIEN);
				commands.add(DIS_BONJOUR_A);
				commands.add(DIS_AU_REVOIR_A);
				commands.add(AU_REVOIR);
			} else {
				commands.add("hello");
				commands.add("hi");
				commands.add("hey");

				commands.add(HOW_ARE_YOU);
				commands.add(SAY_HELLO_TO);
				commands.add(SAY_GOOD_BYE_TO);
				commands.add(GOODBYE);
			}
		}
		return commands;
	}

	@Override
	public String handleRecoveryService(final String sentence, final Locale lang, final HistoryTree historyTree) {
		if (lang == Locale.FRANCE) {
			if (sentence.contains("et toi")) {
				return "Je vais toujours bien";
			}
		}

		if (sentence.contains("what about you") || sentence.contains("and you")) {
			return "I'm always good";
		}

		return null;
	}
}