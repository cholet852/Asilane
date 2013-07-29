package com.darkprograms.speech.synthesiser;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.sun.jndi.toolkit.url.UrlUtil;

/**
 * Synthesiser class that connects to Google's unoffical API to retreive data
 * 
 * @author Luke Kuza
 */
public class Synthesiser {

	/**
	 * URL to query for Google synthesiser
	 */
	private final static String GOOGLE_SYNTHESISER_URL = "http://translate.google.com/translate_tts?q=";

	/**
	 * Constructor
	 */
	public Synthesiser() {
	}

	/**
	 * Gets an input stream to MP3 data for the returned information from a request
	 * 
	 * @param synthText
	 *            Text you want to be synthesized into MP3 data
	 * @return Returns an input stream of the MP3 data that is returned from Google
	 * @throws Exception
	 *             Throws exception if it can not complete the request
	 */
	public InputStream getMP3Data(final String synthText, final String language) throws Exception {
		final String encoded = UrlUtil.encode(synthText + "&tl=" + language, "UTF-8"); // Encode

		final URL url = new URL(GOOGLE_SYNTHESISER_URL + encoded); // create url

		// Open New URL connection channel.
		final URLConnection urlConn = url.openConnection(); // Open connection

		urlConn.addRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:2.0) Gecko/20100101 Firefox/4.0"); // Adding header for user
																							// agent is required
		return urlConn.getInputStream();
	}
}
