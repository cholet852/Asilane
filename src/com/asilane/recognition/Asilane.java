package com.asilane.recognition;

import com.asilane.facade.Facade;

/**
 * Record voice, and recognize what it is saying
 * 
 * @author walane
 */
public class Asilane {

	// For the moment this is the role of terminal

	public static void main(final String[] args) {
		if (args.length < 2 || args[0] == null || args[0].trim().isEmpty() || args[1] == null
				|| args[1].trim().isEmpty()) {
			return;
		}

		final Language language = (args[0].equals("french")) ? Language.french : Language.english;

		System.out.println(Facade.handleSentence(args[1], language));
	}
}
