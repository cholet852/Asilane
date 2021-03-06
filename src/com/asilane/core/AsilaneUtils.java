/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.asilane.core.facade.Question;
import com.asilane.core.facade.Response;
import com.asilane.core.facade.Translator;

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
	 * @param question
	 * @return RegexVarsResult @see {@link RegexVarsResult}
	 */
	public static RegexVarsResult extractRegexVars(final String regex, final Question question) {
		// Use the Matcher to extract vars
		final Matcher matcher = Pattern.compile(regex).matcher(question.getQuestion());

		// If there is no any match
		try {
			if (!matcher.matches()) {
				return null;
			}
		} catch (final PatternSyntaxException e) {
			return null;
		}

		return new RegexVarsResult(matcher);
	}

	/**
	 * Handle static commands to avoid duplicate code
	 * 
	 * @param staticCommands
	 * @param question
	 * @param translator
	 * @return the response from the static command corresponding to the question
	 */
	public static Response handleStaticCommands(final Object[] staticCommands, final Question question, final Translator translator) {

		for (final Object command : staticCommands) {
			if (AsilaneUtils.extractRegexVars(translator.getQuestion(command), question) != null) {
				return new Response(translator.getAnswer(command));
			}
		}

		return null;
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
			final BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			builder = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
		} catch (final IOException e) {
			return e.getMessage();
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
			// Test with google.com
			return InetAddress.getByName("173.194.40.103").isReachable(4000);
		} catch (final Exception e) {
			return false;
		}
	}

	/**
	 * Encode a string for inclusion in a URI (according to RFC 2396).
	 * 
	 * Unsafe characters are escaped by encoding them in three-character sequences '%xy', where 'xy' is the two-digit
	 * hexadecimal representation of the lower 8-bits of the character.
	 * 
	 * The question mark '?' character is also escaped, as required by RFC 2255.
	 * 
	 * The string is first converted to the specified encoding. For LDAP (2255), the encoding must be UTF-8.
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public static String encode(final String s) throws UnsupportedEncodingException {
		final byte[] bytes = s.trim().getBytes("UTF-8");
		final int count = bytes.length;

		/*
		 * From RFC 2396:
		 * 
		 * mark = "-" | "_" | "." | "!" | "~" | "*" | "'" | "(" | ")" reserved = ";" | "/" | ":" | "?" | "@" | "&" | "="
		 * | "+" | "$" | ","
		 */
		final String allowed = "=,+;.'-@&/$_()!~*:"; // '?' is omitted
		final char[] buf = new char[3 * count];
		int j = 0;

		for (int i = 0; i < count; i++) {
			if ((bytes[i] >= 0x61 && bytes[i] <= 0x7A) || // a..z
					(bytes[i] >= 0x41 && bytes[i] <= 0x5A) || // A..Z
					(bytes[i] >= 0x30 && bytes[i] <= 0x39) || // 0..9
					(allowed.indexOf(bytes[i]) >= 0)) {
				buf[j++] = (char) bytes[i];
			} else {
				buf[j++] = '%';
				buf[j++] = Character.forDigit(0xF & (bytes[i] >>> 4), 16);
				buf[j++] = Character.forDigit(0xF & bytes[i], 16);
			}
		}
		return new String(buf, 0, j);
	}
}