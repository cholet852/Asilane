package com.darkprograms.speech.microphone;

import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

/**
 * Microphone class that contains methods to capture audio from microphone
 * 
 * @author Luke Kuza
 */
public class Microphone {

	/**
	 * TargetDataLine variable to receive data from microphone
	 */
	private TargetDataLine targetDataLine;

	/**
	 * Enum for current Microphone state
	 */
	public enum CaptureState {
		PROCESSING_AUDIO, STARTING_CAPTURE, CLOSED
	}

	/**
	 * Variable for enum
	 */
	CaptureState state;

	/**
	 * Variable for the audios saved file type
	 */
	private AudioFileFormat.Type fileType;

	/**
	 * Variable that holds the saved audio file
	 */
	private File audioFile;

	/**
	 * Gets the current state of Microphone
	 * 
	 * @return PROCESSING_AUDIO is returned when the Thread is recording Audio and/or saving it to a file<br>
	 *         STARTING_CAPTURE is returned if the Thread is setting variables<br>
	 *         CLOSED is returned if the Thread is not doing anything/not capturing audio
	 */
	public CaptureState getState() {
		return state;
	}

	/**
	 * Sets the current state of Microphone
	 * 
	 * @param state
	 *            State from enum
	 */
	private void setState(final CaptureState state) {
		this.state = state;
	}

	public File getAudioFile() {
		return audioFile;
	}

	public void setAudioFile(final File audioFile) {
		this.audioFile = audioFile;
	}

	public AudioFileFormat.Type getFileType() {
		return fileType;
	}

	public void setFileType(final AudioFileFormat.Type fileType) {
		this.fileType = fileType;
	}

	public TargetDataLine getTargetDataLine() {
		return targetDataLine;
	}

	public void setTargetDataLine(final TargetDataLine targetDataLine) {
		this.targetDataLine = targetDataLine;
	}

	/**
	 * Constructor
	 * 
	 * @param fileType
	 *            File type to save the audio in<br>
	 *            Example, to save as WAVE use AudioFileFormat.Type.WAVE
	 */
	public Microphone(final AudioFileFormat.Type fileType) {
		setState(CaptureState.CLOSED);
		setFileType(fileType);
	}

	/**
	 * Captures audio from the microphone and saves it a file
	 * 
	 * @param audioFile
	 *            The File to save the audio to
	 * @throws Exception
	 *             Throws an exception if something went wrong
	 */
	public void captureAudioToFile(final File audioFile) throws Exception {
		setState(CaptureState.STARTING_CAPTURE);
		setAudioFile(audioFile);

		final DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, getAudioFormat());
		setTargetDataLine((TargetDataLine) AudioSystem.getLine(dataLineInfo));

		// Get Audio
		new Thread(new CaptureThread()).start();

	}

	/**
	 * Captures audio from the microphone and saves it a file
	 * 
	 * @param audioFile
	 *            The fully path (String) to a file you want to save the audio in
	 * @throws Exception
	 *             Throws an exception if something went wrong
	 */
	public void captureAudioToFile(final String audioFile) throws Exception {
		setState(CaptureState.STARTING_CAPTURE);
		final File file = new File(audioFile);
		setAudioFile(file);

		final DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, getAudioFormat());
		setTargetDataLine((TargetDataLine) AudioSystem.getLine(dataLineInfo));

		// Get Audio
		new Thread(new CaptureThread()).start();

	}

	/**
	 * The audio format to save in
	 * 
	 * @return Returns AudioFormat to be used later when capturing audio from microphone
	 */
	private AudioFormat getAudioFormat() {
		final float sampleRate = 8000.0F;
		// 8000,11025,16000,22050,44100
		final int sampleSizeInBits = 16;
		// 8,16
		final int channels = 1;
		// 1,2
		final boolean signed = true;
		// true,false
		final boolean bigEndian = false;
		// true,false
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}

	/**
	 * Close the microphone capture, saving all processed audio to the specified file.<br>
	 * If already closed, this does nothing
	 */
	public void close() {
		if (getState() == CaptureState.CLOSED) {
		} else {
			getTargetDataLine().stop();
			getTargetDataLine().close();
		}
	}

	/**
	 * Thread to capture the audio from the microphone and save it to a file
	 */
	private class CaptureThread implements Runnable {

		/**
		 * Run method for thread
		 */
		@Override
		public void run() {
			try {
				setState(CaptureState.PROCESSING_AUDIO);
				final AudioFileFormat.Type fileType = getFileType();
				final File audioFile = getAudioFile();
				getTargetDataLine().open(getAudioFormat());
				getTargetDataLine().start();
				AudioSystem.write(new AudioInputStream(getTargetDataLine()), fileType, audioFile);
				setState(CaptureState.CLOSED);
			} catch (final Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}