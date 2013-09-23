package com.asilane.core.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.asilane.service.AsilaneDialogService;
import com.asilane.service.AsilaneIdentityService;
import com.asilane.service.DateService;
import com.asilane.service.FindPlaceService;
import com.asilane.service.FortyTwoService;
import com.asilane.service.HelloService;
import com.asilane.service.IPService;
import com.asilane.service.IService;
import com.asilane.service.InsultService;
import com.asilane.service.MailService;
import com.asilane.service.RepeatService;
import com.asilane.service.SaveWhatSayingService;
import com.asilane.service.WeatherForecastService;
import com.asilane.service.WebBrowserService;
import com.asilane.service.WikipediaService;
import com.asilane.service.YouTubeService;

/**
 * This class find what service have to be called with the sentence <br>
 * It implements Singleton pattern for better performances
 * 
 * @author walane
 * 
 */
public class ServiceDispatcher {
	private static ServiceDispatcher INSTANCE;
	private final Locale lang;
	private List<IService> services;

	private ServiceDispatcher(final Locale lang) {
		this.lang = lang;
		initServices();
	}

	public static ServiceDispatcher getInstance(final Locale lang) {
		// If there is no instance or if the lang is not the same than the local instance
		if (INSTANCE == null || INSTANCE.lang != lang) {
			INSTANCE = new ServiceDispatcher(lang);
		}
		return INSTANCE;
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
		// If nothing found in the first time the service research is extended
		final IService service = getService(sentence, false);

		if (service == null) {
			return getService(sentence, true);
		}

		return service;
	}

	private IService getService(final String sentence, final boolean extendedSearch) {
		final String extended = (extendedSearch) ? ".*" : "";

		for (final IService service : services) {
			for (final String regexService : service.getCommands(lang)) {
				if (sentence.matches(extended + regexService + extended)) {
					return service;
				}
			}
		}

		return null;
	}

	/**
	 * Initall services
	 * 
	 */
	private void initServices() {
		services = new ArrayList<IService>();

		services.add(new MailService());
		services.add(new FindPlaceService());
		services.add(new SaveWhatSayingService());
		services.add(new RepeatService());
		services.add(new FortyTwoService());
		services.add(new YouTubeService());
		services.add(new AsilaneIdentityService());
		services.add(new WeatherForecastService());
		services.add(new WebBrowserService());
		services.add(new DateService());
		services.add(new WikipediaService());
		services.add(new IPService());
		services.add(new AsilaneDialogService());
		services.add(new InsultService());
		services.add(new HelloService());
	}

	/**
	 * Get all services
	 * 
	 * @return All services
	 */
	public List<IService> getAllServices() {
		return services;
	}

	/**
	 * Set a custom service list to the ServiceDispatcher<br>
	 * Useful for adding Android services for example
	 * 
	 * @param otherServices
	 */
	public void setServices(final List<IService> services) {
		this.services = services;
	}
}