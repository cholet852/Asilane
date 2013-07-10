package com.asilane.service;

import java.util.HashSet;
import java.util.Set;

import com.asilane.recognition.Language;

/**
 * @author walane
 * 
 */
public class Hello implements Service {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Language)
	 */
	@Override
	public String handleService(final String sentence, final Language language) {
		if (language == Language.french) {
			return "Bonjour !";
		}

		return "Hello !";
	}

	@Override
	public Set<String> getCommands(final Language lang) {
		final Set<String> set = new HashSet<String>();

		if (lang == Language.french) {
			set.add("bonjour");
			set.add("salut");
		} else {
			set.add("hello");
			set.add("hi");
		}

		return set;
	}
}