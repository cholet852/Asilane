/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.asilane.core;

import java.net.URI;

/**
 * 
 * Specific methods for the client plateform to implement in each Asilane client.
 * 
 * @author walane
 * 
 */
public interface EnvironmentTools {

	/**
	 * Place a String in the clipboard, and make this class the owner of the Clipboard's contents.
	 * 
	 * @param aString
	 *            - the String to copy in the clipboard
	 */
	void setClipboardContents(final String s);

	/**
	 * Launches the default browser to display a URI.
	 * 
	 * @param uri
	 *            - the URI to be displayed in the user default browser
	 * @return true if the browser can be launched, false if not
	 */
	boolean browse(final URI uri);

	/**
	 * Send a mail
	 * 
	 * @param dest
	 *            - the destinataire of the mail
	 * @param subject
	 *            - the mail subject
	 * @param message
	 *            - the message
	 * @return true if the mail client can be launched, false if not
	 */
	boolean mail(final String dest, final String subject, final String message);
}