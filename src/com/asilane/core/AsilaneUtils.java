package com.asilane.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Some good things used in the application
 * 
 * @author walane
 */
public class AsilaneUtils {

	/**
	 * Extract all variables which are in the regex
	 * 
	 * @param regex
	 * @param sentence
	 * @return a list which contains all variables which are in the regex
	 */
	public static List<String> extractRegexVars(final String regex, final String sentence) {
		String regexCleaned = regex.replace(".*", "(.*)");

		// Enlarge regex to expand performances
		if (!regex.startsWith(".*")) {
			regexCleaned = ".*" + regexCleaned;
		}
		if (!regex.endsWith(".*")) {
			regexCleaned = regexCleaned + ".*";
		}

		final Pattern pattern = Pattern.compile(regexCleaned);
		final Matcher matcher = pattern.matcher(sentence);

		// If there is no any match
		if (!matcher.matches()) {
			return null;
		}

		// If not, adding extract all variables in a List
		final List<String> results = new ArrayList<String>();
		for (int i = 1; i <= matcher.groupCount(); i++) {
			results.add(matcher.group(i).trim());
		}

		return results;
	}

	/**
	 * This will return the content of a web page
	 * 
	 * @param address
	 * @return the content of the web page given by the address param
	 */
	public static String curl(final String address) {
		InputStream is = null;
		String line;
		final StringBuilder builder;

		try {
			final URL url = new URL(address);
			is = url.openStream();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			builder = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
		} catch (final IOException e) {
			return null;
		} finally {
			try {
				is.close();
			} catch (final IOException ioe) {
				return null;
			} catch (final NullPointerException e) {
				return null;
			}
		}
		return builder.toString();
	}

	/**
	 * This will return true if the application is connect to the Internet, false not.
	 * 
	 * @return true if the application is connect to the Internet, false not.
	 */
	public static boolean isConnectedToInternet() {
		try {
			// Test with cloudfare.net
			return InetAddress.getByName("69.43.161.171").isReachable(4000);
		} catch (final IOException e) {
			return false;
		}
	}
}