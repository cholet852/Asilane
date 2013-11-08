/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.asilane.core.TextToSpeechThread;
import com.darkprograms.speech.microphone.Microphone.CaptureState;

/**
 * @author walane
 * 
 */
public class Controller implements ActionListener {
	private final GUI gui;

	/**
	 * @param gui
	 */
	public Controller(final GUI gui) {
		this.gui = gui;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		// Stop speech
		TextToSpeechThread.getInstance().stopSpeech();

		// Manual call
		if (!gui.getTextField().getText().isEmpty() && !e.getActionCommand().contains("Record")) {
			gui.getTextFieldResponse().setText(gui.getAsilane().handleSentence(gui.getTextField().getText()));
			return;
		}

		// Voice call
		if (CaptureState.PROCESSING_AUDIO.equals(gui.getAsilane().getRecordingState())) {
			final String iaResponse = gui.getAsilane().closeRecordAndHandleSentence();
			gui.getBtnRecord().setText("Record");
			gui.getTextFieldResponse().setText(iaResponse);
		} else {
			gui.getBtnRecord().setText("Recording...");
			gui.getAsilane().beginRecord();
		}
	}
}