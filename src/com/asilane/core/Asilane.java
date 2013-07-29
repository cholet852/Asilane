package com.asilane.core;

import javax.sound.sampled.AudioFileFormat;

import javazoom.jl.player.Player;

import com.asilane.core.facade.Facade;
import com.asilane.gui.GUI;
import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.microphone.Microphone.CaptureState;
import com.darkprograms.speech.recognizer.Recognizer;
import com.darkprograms.speech.synthesiser.PlayerThread;
import com.darkprograms.speech.synthesiser.Synthesiser;

/**
 * Record voice, play and return the IA response
 * 
 * @author walane
 */
public class Asilane {
	private static final String SAVED_WAV = "voice.wav";
	private final Facade facade;
	private Microphone microphone;
	private Language lang;

	/**
	 * Create a new Asilane Instance
	 */
	public Asilane() {
		facade = new Facade();
		lang = Language.french;
	}

	/**
	 * Start the capture of the microphone
	 */
	public void beginRecord() {
		// Record what is saying
		try {
			microphone = new Microphone(AudioFileFormat.Type.WAVE);
			microphone.captureAudioToFile(SAVED_WAV);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stop the capture of the microphone and if the sentence is understood, a response will be played and returned
	 * 
	 * @return The IA response if the sentence has been understood<br>
	 *         null if not
	 */
	public String closeRecordAndHandleSentence() {
		try {
			// Stop microphone capture
			microphone.close();
		} catch (final Exception e) {
			return null;
		}

		// Transform voice into text
		final String textSpeeched = speechToText(SAVED_WAV);
		if (textSpeeched != null) {
			final String iaResponse = facade.handleSentence(textSpeeched, lang); // Understand what means the sentence
			textToSpeech(iaResponse); // Say the response

			return iaResponse; // Return the response;
		}

		// If nothing has been heard
		if (lang == Language.french) {
			return "Rien n'a été entendu.";
		}
		return "Nothing has been head.";
	}

	/**
	 * Transform a WAV File into a text
	 * 
	 * @return The text corresponding to the WAV File if is understood<br>
	 *         null if not
	 */
	private String speechToText(final String waveFile) {
		final Recognizer recognizer = new Recognizer();
		recognizer.setLanguage(lang.toString().substring(0, 2));

		try {
			return recognizer.getRecognizedDataForWave(waveFile).getResponse();
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * Speech a text in argument
	 * 
	 * @param textToSpeech
	 */
	private boolean textToSpeech(final String textToSpeech) {
		try {
			final Synthesiser synthesiser = new Synthesiser();
			final Player player = new Player(synthesiser.getMP3Data(textToSpeech, lang.toString().substring(0, 2)));
			new PlayerThread(player).start();
		} catch (final Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Direct sentence handling without voice recognition
	 * 
	 * @param sentence
	 * @return The response of the IA corresponding to the sentence
	 */
	public String handleSentence(final String sentence) {
		final String iaRespone = facade.handleSentence(sentence, lang);
		textToSpeech(iaRespone);
		return iaRespone;
	}

	/**
	 * @return the lang
	 */
	public Language getLanguage() {
		return lang;
	}

	/**
	 * @param lang
	 *            the lang to set
	 */
	public void setLanguage(final Language lang) {
		this.lang = lang;
	}

	/**
	 * Gets the current state of Microphone
	 * 
	 * @return PROCESSING_AUDIO is returned when the Thread is recording Audio and/or saving it to a file<br>
	 *         STARTING_CAPTURE is returned if the Thread is setting variables<br>
	 *         CLOSED is returned if the Thread is not doing anything/not capturing audio
	 */
	public CaptureState getRecordingState() {
		return (microphone == null) ? null : microphone.getState();
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		new GUI(new Asilane());
	}
}