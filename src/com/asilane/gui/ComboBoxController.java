/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

public class ComboBoxController implements ActionListener {
	private final GUI gui;

	/**
	 * @param gui
	 */
	public ComboBoxController(final GUI gui) {
		this.gui = gui;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		final String textLang = gui.getLocaleComboBox().getModel().getSelectedItem().toString();
		if (textLang.equals("French")) {
			gui.getAsilane().setLocale(Locale.FRANCE);
			gui.getManualTypeLabel().setText("Posez une question à l'écrit ou appuyez sur Record :");
		} else if (textLang.equals("English")) {
			gui.getAsilane().setLocale(Locale.ENGLISH);
			gui.getManualTypeLabel().setText("Type a question or push Record:");
		}
	}
}