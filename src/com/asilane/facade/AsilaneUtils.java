package com.asilane.facade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Some good things used in the application
 * 
 * @author walane
 */
public class AsilaneUtils {

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
		} catch (final MalformedURLException mue) {
			return null;
		} catch (final IOException ioe) {
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
}