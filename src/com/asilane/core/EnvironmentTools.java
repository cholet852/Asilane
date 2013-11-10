/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.asilane.core;

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
	 */
	void setClipboardContents(final String s);
}