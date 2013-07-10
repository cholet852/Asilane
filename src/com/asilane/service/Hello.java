package com.asilane.service;

import java.util.HashSet;
import java.util.Set;

import com.asilane.recognition.Language;

/**
 * @author walane
 * 
 */
public class Hello implements IService {

	private static final String HOW_ARE_YOU = "how are you";
	private static final String TU_VA_BIEN = "tu va.* bien";
	private static final String COMMENT_VAS_TU = "comment va.*-tu";

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
			}

			return "Bonjour !";
		}

		if (sentence.matches(HOW_ARE_YOU)) {
			return "I'm always good.";
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
		} else {
			set.add("hello");
			set.add("hi");
			set.add("hey");

			set.add(HOW_ARE_YOU);
		}

		return set;
	}
}