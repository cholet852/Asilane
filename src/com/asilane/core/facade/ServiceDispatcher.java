/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.core.facade;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import com.asilane.core.AsilaneUtils;
import com.asilane.core.EnvironmentTools;
import com.asilane.core.IService;
import com.asilane.core.RegexVarsResult;

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
	private final Map<IService, Properties> translationMap;

	/**
	 * @see EnvironmentTools
	 */
	private EnvironmentTools environmentTools;

	private ServiceDispatcher(final Locale lang) {
		this.lang = lang;
		initServices();
		translationMap = new HashMap<IService, Properties>();
		environmentTools = null;
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
		try {
			final URL urlList[];
			urlList = new URL[] { new File("/home/walane/Documents/dev/Asilane/src/services/AsilaneDialogService.jar").toURL() };

			final ClassLoader loader = new URLClassLoader(urlList);

			services.add((IService) Class.forName("com.asilane.service.AsilaneDialog.AsilaneDialogService", true, loader).newInstance());
		} catch (final MalformedURLException e) {
			new RuntimeException(e);
		} catch (final InstantiationException e) {
			new RuntimeException(e);
		} catch (final IllegalAccessException e) {
			new RuntimeException(e);
		} catch (final ClassNotFoundException e) {
			new RuntimeException(e);
		}
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

	/**
	 * Return the environment tools
	 * 
	 * @return the environmentTools
	 * @see EnvironmentTools
	 */
	public EnvironmentTools getEnvironmentTools() {
		return environmentTools;
	}

	/**
	 * Set the environment tools
	 * 
	 * @param environmentTools
	 *            the environmentTools to set
	 * @see EnvironmentTools
	 */
	public void setEnvironmentTools(final EnvironmentTools environmentTools) {
		this.environmentTools = environmentTools;
	}

	public static void main(final String[] args) {
		ServiceDispatcher.getInstance(Locale.FRENCH);
	}
}