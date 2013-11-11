/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.core.facade;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
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
	 * Init all services
	 * 
	 */
	private IService initServices() {
		services = new ArrayList<IService>();
		try {

			final Properties servicesToExcept = new Properties();
			servicesToExcept.load(getClass().getResourceAsStream("/services/services.properties"));

			final File[] jarFiles = searchFiles(".jar");

			// Add each service to the list
			for (final File jarFile : jarFiles) {
				final String className = jarFile.getName().replace(".jar", "");

				if (!servicesToExcept.contains(className)) {
					final URL urlList[] = new URL[] { jarFile.toURI().toURL() };

					final ClassLoader loader = new URLClassLoader(urlList);
					final IService service = (IService) Class.forName(
							"com.asilane.service." + className.replace("Service", "") + "." + className, true, loader).newInstance();

					services.add(service);
					return service;
				}
			}
		} catch (final InstantiationException e) {
			new RuntimeException(e);
		} catch (final IllegalAccessException e) {
			new RuntimeException(e);
		} catch (final ClassNotFoundException e) {
			new RuntimeException(e);
		} catch (final IOException e) {
			new RuntimeException(e);
		} catch (final URISyntaxException e) {
			new RuntimeException(e);
		}
		return null;
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
				final File[] jarFiles = searchFiles(".jar");
				final URL urlList[] = new URL[] { jarFiles[0].toURI().toURL() };

				@SuppressWarnings("resource")
				final ClassLoader loader = new URLClassLoader(urlList);
				final InputStream is = loader.getResourceAsStream("i18n/" + lang.toLanguageTag() + ".properties");

				final Properties tmpPropertyFile = new Properties();
				tmpPropertyFile.load(is);
				is.close();

				// Add the properties file to the translation map
				translationMap.put(service, tmpPropertyFile);
				propertyFile = tmpPropertyFile;
			} catch (final IOException ioe) {
				return null;
			} catch (final URISyntaxException e) {
				return null;
			}
		}

		return new Translator(propertyFile);
	}

	private File[] searchFiles(final String filter) throws URISyntaxException {
		final File[] files = new File(getClass().getResource("/services/").toURI()).listFiles(new FileFilter() {
			@Override
			public boolean accept(final File file) {
				return file.toString().endsWith(filter);
			}
		});
		return files;
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
}