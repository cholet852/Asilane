package com.asilane.core.facade;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import com.asilane.core.AsilaneUtils;
import com.asilane.core.RegexVarsResult;
import com.asilane.service.IService;
import com.asilane.service.AsilaneDialog.AsilaneDialogService;
import com.asilane.service.AsilaneIdentity.AsilaneIdentityService;
import com.asilane.service.Date.DateService;
import com.asilane.service.FindPlace.FindPlaceService;
import com.asilane.service.FortyTwo.FortyTwoService;
import com.asilane.service.IP.IPService;
import com.asilane.service.Insult.InsultService;
import com.asilane.service.Mail.MailService;
import com.asilane.service.Repeat.RepeatService;
import com.asilane.service.SaveWhatISay.SaveWhatISayService;
import com.asilane.service.WeatherForecast.WeatherForecastService;
import com.asilane.service.WebBrowser.WebBrowserService;
import com.asilane.service.Wikipedia.WikipediaService;
import com.asilane.service.YouTube.YouTubeService;

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
		Translator translator;
		RegexVarsResult regexVars;

		for (final IService service : services) {
			translator = getTranslation(service);

			for (final Object regex : translator.values()) {
				regexVars = AsilaneUtils.extractRegexVars(regex.toString(), new Question(sentence, lang));

				if (regexVars != null && !regex.toString().contains("RECOVERY")) {
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
		services.add(new SaveWhatISayService());
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
	}

	/**
	 * Get the translation properties file of a service
	 * 
	 * @param service
	 * @return the translation of the service
	 */
	public Translator getTranslation(final IService service) {
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

		return new Translator(propertyFile);
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