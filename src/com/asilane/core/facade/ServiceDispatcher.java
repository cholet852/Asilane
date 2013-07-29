package com.asilane.core.facade;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.asilane.core.Language;
import com.asilane.service.AsilaneDialogService;
import com.asilane.service.AsilaneIdentityService;
import com.asilane.service.DateService;
import com.asilane.service.FindPlaceService;
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
 * 
 */
public class ServiceDispatcher {
	private static ServiceDispatcher INSTANCE;
	private static Map<IService, Set<String>> commandsMap;
	private final Language lang;

	private ServiceDispatcher(final Language lang) {
		initMaps(lang);
		this.lang = lang;
	}

	public static ServiceDispatcher getInstance(final Language lang) {
		// If there is no instance or if the lang is not the same than the local instance
		if (INSTANCE == null || INSTANCE.lang != lang) {
			INSTANCE = new ServiceDispatcher(lang);
		}
		return INSTANCE;
	}

	private void initMaps(final Language lang) {
		commandsMap = new HashMap<IService, Set<String>>();

		for (final IService service : getAllServices()) {
			commandsMap.put(service, service.getCommands(lang));
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
		// TODO : improve performances
		for (final IService service : commandsMap.keySet()) {
			for (final String regexService : commandsMap.get(service)) {
				if (sentence.matches(".*" + regexService + ".*")) {
					return service;
				}
			}
		}

		return null;
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
		allServices.add(new WikipediaService());
		allServices.add(new FindPlaceService());

		return allServices;
	}
}