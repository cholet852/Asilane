package com.asilane.core.facade;

import java.util.Locale;

public class NoServiceFoundException extends Exception {
	private static final long serialVersionUID = -2527600166230941073L;
	private final Locale lang;

	/**
	 * Create a NoServiceFoundException
	 * 
	 * @param lang
	 */
	public NoServiceFoundException(final Locale lang) {
		this.lang = lang;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		if (lang == Locale.FRANCE) {
			return "Je n'ai pas bien compris, pouvez-vous répéter ?";
		}
		return "I don't understand, can you repeat?";
	}
}
