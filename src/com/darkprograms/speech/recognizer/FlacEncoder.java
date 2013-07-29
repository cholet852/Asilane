package com.darkprograms.speech.recognizer;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javaFlacEncoder.FLACEncoder;
import javaFlacEncoder.FLACFileOutputStream;
import javaFlacEncoder.StreamConfiguration;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 * Class that contains methods to encode Wave files to FLAC files THIS IS THANKS TO THE javaFlacEncoder Project created
 * here: http://sourceforge.net/projects/javaflacencoder/
 */
public class FlacEncoder {

	/**
	 * Constructor
	 */
	public FlacEncoder() {

	}

	/**
	 * Converts a wave file to a FLAC file(in order to POST the data to Google and retrieve a response) <br>
	 * Sample Rate is 8000 by default
	 * 
	 * @param inputFile
	 *            Input wave file
	 * @param outputFile
	 *            Output FLAC file
	 */
	public void convertWaveToFlac(final File inputFile, final File outputFile) {

		final StreamConfiguration streamConfiguration = new StreamConfiguration();
		streamConfiguration.setSampleRate(8000);
		streamConfiguration.setBitsPerSample(16);
		streamConfiguration.setChannelCount(1);

		try {
			final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputFile);
			final AudioFormat format = audioInputStream.getFormat();

			final int frameSize = format.getFrameSize();

			final FLACEncoder flacEncoder = new FLACEncoder();
			final FLACFileOutputStream flacOutputStream = new FLACFileOutputStream(outputFile);

			flacEncoder.setStreamConfiguration(streamConfiguration);
			flacEncoder.setOutputStream(flacOutputStream);

			flacEncoder.openFLACStream();
			// if (audioInputStream.getFrameLength() <= 0) {
			// return;
			// }
			final int[] sampleData = new int[(int) audioInputStream.getFrameLength()];

			final byte[] samplesIn = new byte[frameSize];

			int i = 0;

			while (audioInputStream.read(samplesIn, 0, frameSize) != -1) {
				if (frameSize != 1) {
					final ByteBuffer bb = ByteBuffer.wrap(samplesIn);
					bb.order(ByteOrder.LITTLE_ENDIAN);
					final short shortVal = bb.getShort();
					sampleData[i] = shortVal;
				} else {
					sampleData[i] = samplesIn[0];
				}

				i++;
			}

			flacEncoder.addSamples(sampleData, i);
			flacEncoder.encodeSamples(i, false);
			flacEncoder.encodeSamples(flacEncoder.samplesAvailableToEncode(), true);

			audioInputStream.close();
			flacOutputStream.close();
			flacEncoder.clear();
			inputFile.delete();
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Converts a wave file to a FLAC file(in order to POST the data to Google and retrieve a response) <br>
	 * Sample Rate is 8000 by default
	 * 
	 * @param inputFile
	 *            Input wave file
	 * @param outputFile
	 *            Output FLAC file
	 */
	public void convertWaveToFlac(final String inputFile, final String outputFile) {
		convertWaveToFlac(new File(inputFile), new File(outputFile));
	}
}