package com.asilane.facade;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.asilane.recognition.Language;
import com.asilane.service.AsilaneDialogService;
import com.asilane.service.AsilaneIdentityService;
import com.asilane.service.CalculatorService;
import com.asilane.service.DateService;
import com.asilane.service.FortyTwoService;
import com.asilane.service.HelloService;
import com.asilane.service.IPService;
import com.asilane.service.IService;
import com.asilane.service.MediaPlayerService;
import com.asilane.service.WeatherForecastService;
import com.asilane.service.WebBrowserService;
import com.asilane.service.WikipediaService;

/**
 * This class find what service have to be called with the sentence <br>
 * It implements Singleton pattern for better performances
 * 
 * @author walane
 */
public class ServiceDispatcher {
	private static ServiceDispatcher INSTANCE;
	private static Map<List<String>, IService> wordMap;

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
		wordMap = new HashMap<List<String>, IService>();

		// Adding each word of each sentence of each service in the wordMap
		for (final IService service : getAllServices()) {
			for (final String sentence : service.getCommands(lang)) {
				wordMap.put(Arrays.asList(sentence.split(" ")), service);
			}
		}
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
		// Map of occurencies
		final Map<IService, Integer> occurenciesMap = new HashMap<IService, Integer>();

		// Getting all words of the sentence
		final String[] words = sentence.split(" ");

		// The service which have the most occurencies of words in the sentence win
		for (final String word : words) {
			System.out.println(">>" + word);
			for (final Entry<List<String>, IService> wordList : wordMap.entrySet()) {
				for (final String mapWord : wordList.getKey()) {
					if (word.equals(mapWord)) {
						System.out.println(word + "<" + wordList.getValue());
						Integer currentOccurence = occurenciesMap.get(wordList.getValue());
						currentOccurence = currentOccurence == null ? 0 : currentOccurence;
						occurenciesMap.put(wordList.getValue(), currentOccurence + 1);
					}
				}
			}
		}

		IService pertinentService = null;
		int maximum = 0;
		for (final Entry<IService, Integer> entry : occurenciesMap.entrySet()) {
			if (entry.getValue() > maximum) {
				maximum = entry.getValue();
				pertinentService = entry.getKey();
			}
		}
		System.out.println(pertinentService);

		return pertinentService;
	}

	/**
	 * Get all services
	 * 
	 * @return All services
	 */
	public Set<IService> getAllServices() {
		final Set<IService> allServices = new HashSet<IService>();

		allServices.add(new HelloService());
		allServices.add(new AsilaneIdentityService());
		allServices.add(new FortyTwoService());
		allServices.add(new WeatherForecastService());
		allServices.add(new WebBrowserService());
		allServices.add(new MediaPlayerService());
		allServices.add(new AsilaneDialogService());
		allServices.add(new DateService());
		allServices.add(new IPService());
		allServices.add(new CalculatorService());
		allServices.add(new WikipediaService());

		return allServices;
	}

	public static void main(final String[] args) {
		ServiceDispatcher.getInstance(Language.french).getService("quel est la météo à vendome");
	}
}