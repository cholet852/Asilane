package com.asilane.core.facade;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import com.asilane.service.IService;
import com.asilane.service.AsilaneDialog.AsilaneDialogService;
import com.asilane.service.AsilaneIdentity.AsilaneIdentityService;

/**
 * This class find what service have to be called with the sentence <br>
 * It implements Singleton pattern for better performances
 * 
 * @author walane
 * 
 */
/**
 * @author walane
 * 
 */
public class ServiceDispatcher {
	private static ServiceDispatcher INSTANCE;
	private final Locale lang;
	private List<IService> services;
	private final Map<IService, Properties> translationMap;

	private ServiceDispatcher(final Locale lang) {
		this.lang = lang;
		initServices();
		translationMap = new HashMap<IService, Properties>();
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
		for (final IService service : services) {
			final Translation translationFile = getTranslation(service);

			for (final Object regex : translationFile.values()) {
				if (sentence.matches(cleanRegex(regex.toString()))) {
					return service;
				}
			}
		}

		return null;
	}

	/**
	 * Clean regex : transform (name .*) to (.*)
	 * 
	 * @param regex
	 * @return the cleaned regex
	 */
	private String cleanRegex(final String regex) {
		// 1. Save the position of each named regex
		boolean inParenthese = false;
		int cptRegex = 0;
		final List<String> namedRegexList = new ArrayList<String>();
		StringBuilder namedRegexTmp = new StringBuilder();

		// Parsing
		for (int i = 0; i < regex.length(); i++) {
			if (inParenthese && regex.charAt(i) == ' ') {
				namedRegexList.add(cptRegex++, namedRegexTmp.toString());
				namedRegexTmp = new StringBuilder();
				inParenthese = false;
			} else if (inParenthese) {
				namedRegexTmp.append(regex.charAt(i));
			} else if (regex.charAt(i) == '(') {
				inParenthese = true;
			} else if (inParenthese && regex.charAt(i) == ')') {
				namedRegexList.add(cptRegex++, null);
				namedRegexTmp = new StringBuilder();
				inParenthese = false;
			}
		}

		// 2. Remove named regex from the regex
		String regexCleaned = regex;
		for (final String regexName : namedRegexList) {
			regexCleaned = regexCleaned.replace(regexName + " ", "");
		}

		return regexCleaned;
	}

	/**
	 * Initall services
	 * 
	 */
	private void initServices() {
		services = new ArrayList<IService>();

		// services.add(new MailService());
		// services.add(new FindPlaceService());
		// services.add(new SaveWhatSayingService());
		// services.add(new RepeatService());
		// services.add(new FortyTwoService());
		// services.add(new YouTubeService());
		services.add(new AsilaneIdentityService());
		// services.add(new WeatherForecastService());
		// services.add(new WebBrowserService());
		// services.add(new DateService());
		// services.add(new WikipediaService());
		// services.add(new IPService());
		services.add(new AsilaneDialogService());
		// services.add(new InsultService());
		// services.add(new HelloService());
	}

	/**
	 * Get the translation properties file of a service
	 * 
	 * @param service
	 * @return the translation of the service
	 */
	public Translation getTranslation(final IService service) {
		Properties propertyFile = translationMap.get(service);

		// If the property file corresponding to the service doesn't exists, get it
		if (propertyFile == null) {
			try {
				final String langParsed = lang.getCountry().isEmpty() ? lang.toString() : lang.getCountry();
				final InputStream is = service.getClass().getResourceAsStream(
						"/com/asilane/service/" + service.getClass().getSimpleName().replace("Service", "") + "/i18n/"
								+ langParsed.toLowerCase() + ".properties");

				final Properties tmpPropertyFile = new Properties();
				tmpPropertyFile.load(is);
				is.close();

				// Add the properties file to the translation map
				translationMap.put(service, tmpPropertyFile);
				propertyFile = tmpPropertyFile;
			} catch (final IOException ioe) {
				return null;
			}
		}

		return new Translation(propertyFile);
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