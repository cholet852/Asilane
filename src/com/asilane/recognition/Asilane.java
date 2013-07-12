package com.asilane.recognition;

import com.asilane.facade.Facade;

/**
 * Record voice, and recognize what it is saying
 * 
 * @author walane
 */
public class Asilane {

	// For the moment this is the role of terminal

	public static void main(final String[] args) {
		if (args.length < 2 || args[0] == null || args[0].trim().isEmpty() || args[1] == null
				|| args[1].trim().isEmpty()) {
			return;
		}

		final Language language = (args[0].equals("french")) ? Language.french : Language.english;

		System.out.println(Facade.handleSentence(args[1], language));

		// Recognition
		/*
		 * Path path = Paths.get("out.flac"); byte[] data = Files.readAllBytes(path);
		 * 
		 * String request = "https://www.google.com/"+ "speech-api/v1/recognize?"+
		 * "xjerr=1&client=speech2text&lang=en-US&maxresults=10"; URL url = new URL(request); HttpURLConnection
		 * connection = (HttpURLConnection) url.openConnection(); connection.setDoOutput(true);
		 * connection.setDoInput(true); connection.setInstanceFollowRedirects(false);
		 * connection.setRequestMethod("POST"); connection.setRequestProperty("Content-Type",
		 * "audio/x-flac; rate=16000"); connection.setRequestProperty("User-Agent", "speech2text");
		 * connection.setConnectTimeout(60000); connection.setUseCaches (false);
		 * 
		 * DataOutputStream wr = new DataOutputStream(connection.getOutputStream ()); wr.write(data); wr.flush();
		 * wr.close(); connection.disconnect();
		 * 
		 * System.out.println("Done");
		 * 
		 * BufferedReader in = new BufferedReader( new InputStreamReader( connection.getInputStream())); String
		 * decodedString; while ((decodedString = in.readLine()) != null) { System.out.println(decodedString); }
		 */

	}
}
