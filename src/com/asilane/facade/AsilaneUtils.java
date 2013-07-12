package com.asilane.facade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;

/**
 * Some good things used in the application
 * 
 * @author walane
 */
public class AsilaneUtils {

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