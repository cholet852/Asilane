package com.asilane.recognition;

import com.asilane.facade.Facade;

/**
 * @author walane
 * 
 *         Description : Record voice, and recognize what it is saying
 */
public class Recognition {

	// For the moment this is the role of terminal

	public static void main(final String[] args) {
		final Language language = (args[1].equals("french")) ? Language.french : Language.english;

		System.out.println(Facade.handleSentence(args[0], language));
	}
}
