package com.asilane.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.asilane.core.Language;

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
		final String textLang = gui.getLanguageComboBox().getModel().getSelectedItem().toString();
		if (textLang.equals("French")) {
			gui.getAsilane().setLanguage(Language.french);
			gui.getManualTypeLabel().setText("Posez une question à l'écrit ou appuyez sur Record :");
		} else if (textLang.equals("English")) {
			gui.getAsilane().setLanguage(Language.english);
			gui.getManualTypeLabel().setText("Type a question or push Record:");
		}
	}
}