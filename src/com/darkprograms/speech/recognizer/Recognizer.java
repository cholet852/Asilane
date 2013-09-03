package com.darkprograms.speech.recognizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Class that submits FLAC audio and retrieves recognized text
 * 
 * @author Luke Kuza, Duncan Jauncey
 */
public class Recognizer {

	/**
	 * URL to POST audio data and retrieve results
	 */
	private static final String GOOGLE_RECOGNIZER_URL = "https://www.google.com/speech-api/v1/recognize?xjerr=1&client=chromium";

	private boolean profanityFilter = true;
	private String Locale = null;

	public static final String LANG_US_ENGLISH = "en-US";
	public static final String LANG_UK_ENGLISH = "en-GB";
	public static final String LANG_FR_FRENCH = "fr-FR";

	/**
	 * Constructor
	 */
	public Recognizer() {
	}

	/**
	 * Enable/disable Google's profanity filter (on by default).
	 * 
	 * @param profanityFilter
	 */
	public void setProfanityFilter(final boolean profanityFilter) {
		this.profanityFilter = profanityFilter;
	}

	/**
	 * Locale code. This Locale code must match the Locale of the speech to be recognized. ex. en-US ru-RU Setting
	 * this to null will make Google use it's own Locale detection. This value is null by default.
	 * 
	 * @param Locale
	 */
	public void setLocale(final String Locale) {
		this.Locale = Locale;
	}

	/**
	 * Get recognized data from a Wave file. This method will encode the wave file to a FLAC
	 * 
	 * @param waveFile
	 *            Wave file to recognize
	 * @param maxResults
	 *            Maximum number of results to return in response
	 * @return Returns a GoogleResponse, with the response and confidence score
	 * @throws Exception
	 *             Throws exception if something goes wrong
	 */
	public GoogleResponse getRecognizedDataForWave(final File waveFile, final int maxResults) throws Exception {
		final FlacEncoder flacEncoder = new FlacEncoder();
		final File flacFile = new File(waveFile + ".flac");

		flacEncoder.convertWaveToFlac(waveFile, flacFile);

		final String response = rawRequest(flacFile, maxResults);

		// Delete converted FLAC data
		flacFile.delete();

		final GoogleResponse googleResponse = new GoogleResponse();
		parseResponse(response, googleResponse);
		return googleResponse;
	}

	/**
	 * Get recognized data from a Wave file. This method will encode the wave file to a FLAC
	 * 
	 * @param waveFile
	 *            Wave file to recognize
	 * @param maxResults
	 *            the maximum number of results to return in the response
	 * @return Returns a GoogleResponse, with the response and confidence score
	 * @throws Exception
	 *             Throws exception if something goes wrong
	 */
	public GoogleResponse getRecognizedDataForWave(final String waveFile, final int maxResults) throws Exception {
		return getRecognizedDataForWave(new File(waveFile), maxResults);
	}

	/**
	 * Get recognized data from a FLAC file.
	 * 
	 * @param flacFile
	 *            FLAC file to recognize
	 * @param maxResults
	 *            the maximum number of results to return in the response
	 * @return Returns a GoogleResponse, with the response and confidence score
	 * @throws Exception
	 *             Throws exception if something goes wrong
	 */
	public GoogleResponse getRecognizedDataForFlac(final File flacFile, final int maxResults) throws Exception {
		final String response = rawRequest(flacFile, maxResults);
		final GoogleResponse googleResponse = new GoogleResponse();
		parseResponse(response, googleResponse);
		return googleResponse;
	}

	/**
	 * Get recognized data from a FLAC file.
	 * 
	 * @param flacFile
	 *            FLAC file to recognize
	 * @param maxResults
	 *            the maximum number of results to return in the response
	 * @return Returns a GoogleResponse, with the response and confidence score
	 * @throws Exception
	 *             Throws exception if something goes wrong
	 */
	public GoogleResponse getRecognizedDataForFlac(final String flacFile, final int maxResults) throws Exception {
		return getRecognizedDataForFlac(new File(flacFile), maxResults);
	}

	/**
	 * Get recognized data from a Wave file. This method will encode the wave file to a FLAC. This method will
	 * automatically set the Locale to en-US, or English
	 * 
	 * @param waveFile
	 *            Wave file to recognize
	 * @return Returns a GoogleResponse, with the response and confidence score
	 * @throws Exception
	 *             Throws exception if something goes wrong
	 */
	public GoogleResponse getRecognizedDataForWave(final File waveFile) throws Exception {
		return getRecognizedDataForWave(waveFile, 1);
	}

	/**
	 * Get recognized data from a Wave file. This method will encode the wave file to a FLAC. This method will
	 * automatically set the Locale to en-US, or English
	 * 
	 * @param waveFile
	 *            Wave file to recognize
	 * @return Returns a GoogleResponse, with the response and confidence score
	 * @throws Exception
	 *             Throws exception if something goes wrong
	 */
	public GoogleResponse getRecognizedDataForWave(final String waveFile) throws Exception {
		return getRecognizedDataForWave(waveFile, 1);
	}

	/**
	 * Get recognized data from a FLAC file. This method will automatically set the Locale to en-US, or English
	 * 
	 * @param flacFile
	 *            FLAC file to recognize
	 * @return Returns a GoogleResponse, with the response and confidence score
	 * @throws Exception
	 *             Throws exception if something goes wrong
	 */
	public GoogleResponse getRecognizedDataForFlac(final File flacFile) throws Exception {
		return getRecognizedDataForFlac(flacFile, 1);
	}

	/**
	 * Get recognized data from a FLAC file. This method will automatically set the Locale to en-US, or English
	 * 
	 * @param flacFile
	 *            FLAC file to recognize
	 * @return Returns a GoogleResponse, with the response and confidence score
	 * @throws Exception
	 *             Throws exception if something goes wrong
	 */
	public GoogleResponse getRecognizedDataForFlac(final String flacFile) throws Exception {
		return getRecognizedDataForFlac(flacFile, 1);
	}

	/**
	 * Parses the raw response from Google
	 * 
	 * @param rawResponse
	 *            The raw, unparsed response from Google
	 * @return Returns the parsed response. Index 0 is response, Index 1 is confidence score
	 */
	private void parseResponse(final String rawResponse, final GoogleResponse googleResponse) {
		if (!rawResponse.contains("utterance")) {
			return;
		}

		final String array = substringBetween(rawResponse, "[", "]");
		final String[] parts = array.split("}");

		boolean first = true;
		for (final String s : parts) {
			if (first) {
				first = false;
				final String utterancePart = s.split(",")[0];
				final String confidencePart = s.split(",")[1];

				String utterance = utterancePart.split(":")[1];
				String confidence = confidencePart.split(":")[1];

				utterance = stripQuotes(utterance);
				confidence = stripQuotes(confidence);

				if (utterance.equals("null")) {
					utterance = null;
				}
				if (confidence.equals("null")) {
					confidence = null;
				}

				googleResponse.setResponse(utterance);
				googleResponse.setConfidence(confidence);
			} else {
				String utterance = s.split(":")[1];
				utterance = stripQuotes(utterance);
				if (utterance.equals("null")) {
					utterance = null;
				}
				googleResponse.getOtherPossibleResponses().add(utterance);
			}
		}
	}

	/**
	 * Performs the request to Google with a file <br>
	 * Request is buffered
	 * 
	 * @param inputFile
	 *            Input files to recognize
	 * @return Returns the raw, unparsed response from Google
	 * @throws Exception
	 *             Throws exception if something went wrong
	 */
	private String rawRequest(final File inputFile, final int maxResults) throws Exception {
		URL url;
		URLConnection urlConn;
		OutputStream outputStream;
		BufferedReader br;

		final StringBuilder sb = new StringBuilder(GOOGLE_RECOGNIZER_URL);
		if (Locale != null) {
			sb.append("&lang=");
			sb.append(Locale);
		}
		if (!profanityFilter) {
			sb.append("&pfilter=0");
		}
		sb.append("&maxresults=");
		sb.append(maxResults);

		// URL of Remote Script.
		url = new URL(sb.toString());

		// Open New URL connection channel.
		urlConn = url.openConnection();

		// we want to do output.
		urlConn.setDoOutput(true);

		// No caching
		urlConn.setUseCaches(false);

		// Specify the header content type.
		urlConn.setRequestProperty("Content-Type", "audio/x-flac; rate=8000");

		// Send POST output.
		outputStream = urlConn.getOutputStream();

		final FileInputStream fileInputStream = new FileInputStream(inputFile);

		final byte[] buffer = new byte[256];

		while ((fileInputStream.read(buffer, 0, 256)) != -1) {
			outputStream.write(buffer, 0, 256);
		}

		fileInputStream.close();
		outputStream.close();

		// Get response data.
		br = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

		final String response = br.readLine();

		br.close();

		return response;

	}

	private String substringBetween(final String s, final String part1, final String part2) {
		String sub = null;

		final int i = s.indexOf(part1);
		final int j = s.indexOf(part2, i + part1.length());

		if (i != -1 && j != -1) {
			final int nStart = i + part1.length();
			sub = s.substring(nStart, j);
		}

		return sub;
	}

	private String stripQuotes(final String s) {
		int start = 0;
		if (s.startsWith("\"")) {
			start = 1;
		}
		int end = s.length();
		if (s.endsWith("\"")) {
			end = s.length() - 1;
		}
		return s.substring(start, end);
	}

}
