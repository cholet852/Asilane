package com.asilane.service;

import java.util.HashSet;
import java.util.Set;

import com.asilane.recognition.Language;

/**
 * @author walane
 * 
 */
public class CalculatorService implements IService {

	private static final String ADDITION_FR = "combien font .* plus .*";
	private static final String ADDITION_EN = "how much are .* plus .*";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Language)
	 */
	@Override
	public String handleService(final String sentence, final Language lang) {
		// Extract the two terms of the addition/division/...
		if (lang == Language.french) {
			if (sentence.matches(ADDITION_FR)) {
				final int term1 = stringToInt(
						sentence.substring(sentence.lastIndexOf("font") + 4, sentence.lastIndexOf("plus")).trim(), lang);
				final int term2 = stringToInt(sentence.substring(sentence.lastIndexOf("plus") + 4).trim(), lang);
				return String.valueOf(term1 + term2);
			}
		}

		if (sentence.matches(ADDITION_EN)) {
			final int term1 = stringToInt(
					sentence.substring(sentence.lastIndexOf("are") + 3, sentence.lastIndexOf("plus")).trim(), lang);
			final int term2 = stringToInt(sentence.substring(sentence.lastIndexOf("plus") + 4).trim(), lang);
			System.out.println(term1 + term2);
			return String.valueOf(term1 + term2);
		}
		return null;
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
			set.add(ADDITION_FR);
		} else {
			set.add(ADDITION_EN);
		}

		return set;
	}

	private int stringToInt(final String number, final Language lang) {
		// FIXME : Use a library to convert this shit
		if (lang == Language.french) {
			if (number.equals("un")) {
				return 1;
			} else if (number.equals("deux")) {
				return 2;
			} else if (number.equals("trois")) {
				return 3;
			} else if (number.equals("quatre")) {
				return 4;
			} else if (number.equals("cinq")) {
				return 5;
			} else if (number.equals("six")) {
				return 6;
			} else if (number.equals("sept")) {
				return 7;
			} else if (number.equals("huit")) {
				return 8;
			} else if (number.equals("neuf")) {
				return 9;
			} else if (number.equals("dix")) {
				return 10;
			}
		}
		return 0;
	}
}