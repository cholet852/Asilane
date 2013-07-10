package com.asilane.recognition;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.asilane.service.AsilaneIdentity;
import com.asilane.service.FortyTwo;
import com.asilane.service.Hello;
import com.asilane.service.IService;

/**
 * This class find what service have to be called with the sentence <br>
 * It implements Singleton pattern for better performances
 * 
 * @author walane
 * 
 */
public class ServiceDispatcher {
	private static ServiceDispatcher INSTANCE;
	private static Map<IService, Set<String>> commandsMap;

	private ServiceDispatcher(final Language lang) {
		initMaps(lang);
	}

	public static ServiceDispatcher getInstance(final Language lang) {
		if (INSTANCE == null) {
			INSTANCE = new ServiceDispatcher(lang);
		}
		return INSTANCE;
	}

	private void initMaps(final Language lang) {
		commandsMap = new HashMap<IService, Set<String>>();
		final IService hello = new Hello();
		final IService asilaneIdentity = new AsilaneIdentity();
		final IService fortyTwo = new FortyTwo();

		// for each language in each service, translations are added in the global map
		commandsMap.put(hello, hello.getCommands(lang));
		commandsMap.put(asilaneIdentity, asilaneIdentity.getCommands(lang));
		commandsMap.put(fortyTwo, fortyTwo.getCommands(lang));
	}

	/**
	 * For each service in commands Map, if the client sentence corresponding to one of sentence in the set of the
	 * service it returns the service
	 * 
	 * @param sentence
	 * @return Service
	 * 
	 */
	public IService getService(final String sentence) {
		for (final IService service : commandsMap.keySet()) {
			for (final String sentenceService : commandsMap.get(service)) {
				if (sentence.matches(sentenceService)) {
					return service;
				}
			}
		}

		return null;
	}
}