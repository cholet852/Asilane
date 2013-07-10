package com.asilane.recognition;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.asilane.service.Hello;
import com.asilane.service.Service;

/**
 * @author walane
 * 
 *         This class implements Singleton pattern for better performances
 */
public class Command {
	private static Command INSTANCE;
	private static Map<Service, Set<String>> commandsMap;

	private Command(final Language lang) {
		initMaps(lang);
	}

	public static Command getInstance(final Language lang) {
		if (INSTANCE == null) {
			INSTANCE = new Command(lang);
		}
		return INSTANCE;
	}

	private void initMaps(final Language lang) {
		commandsMap = new HashMap<Service, Set<String>>();
		final Service hello = new Hello();

		// for each language in each service, translations are added in the global map
		commandsMap.put(hello, hello.getCommands(lang));
	}

	/**
	 * @param sentence
	 * @return Service Description : For each service in commands Map, if the client sentence corresponding to one of
	 *         sentence in the set of the service it returns the service
	 */
	public Service getService(final String sentence) {
		for (final Service service : commandsMap.keySet()) {
			for (final String setSentence : commandsMap.get(service)) {
				if (sentence.equals(setSentence)) {
					return service;
				}
			}
		}

		return null;
	}
}