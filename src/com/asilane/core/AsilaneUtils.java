package com.asilane.core;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
			// Test with cloudfare.net
			return InetAddress.getByName("69.43.161.171").isReachable(4000);
		} catch (final IOException e) {
			return false;
		}
	}

	/**
	 * Place a String on the clipboard, and make this class the owner of the Clipboard's contents.
	 * 
	 * @param aString
	 */
	public static void setClipboardContents(final String aString) {
		// TODO : Android support
		final StringSelection stringSelection = new StringSelection(aString);
		final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, new ClipboardOwner() {
			@Override
			public void lostOwnership(final Clipboard arg0, final Transferable arg1) {
			}
		});
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

	/**
	 * Verify if Desktop exists (especially for Android)
	 * 
	 * @return
	 */
	public static boolean isDesktopSupported() {
		try {
			return Desktop.isDesktopSupported();
		} catch (final NoClassDefFoundError e) {
			return false;
		}
	}
}