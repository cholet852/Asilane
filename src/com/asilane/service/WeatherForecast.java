package com.asilane.service;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.asilane.facade.AsilaneUtils;
import com.asilane.recognition.Language;
import com.sun.jndi.toolkit.url.UrlUtil;

/**
 * @author walane
 * 
 */
public class WeatherForecast implements IService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Language)
	 */
	@Override
	public String handleService(final String sentence, final Language lang) {
		// TODO : Use regular expressions to get the city
		final String city;
		if (lang == Language.french) {
			int cityIndex = 0;
			if (sentence.contains("aux")) {
				cityIndex = sentence.lastIndexOf("aux") + 3;
			} else if (sentence.contains("au")) {
				cityIndex = sentence.lastIndexOf("au") + 2;
			} else if (sentence.contains("à")) {
				cityIndex = sentence.lastIndexOf('à') + 1;
			}
			city = sentence.substring(cityIndex).trim();
		} else {
			city = sentence.substring(sentence.lastIndexOf("in") + 1).trim();
		}

		// The city must no be empty to get the weather forecast
		if (city.isEmpty()) {
			if (lang == Language.french) {
				return "Veuillez spécifier une ville.";
			}
			return "Please specify a city";
		}

		return getWeatherForecast(city, lang);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#getCommands(com.asilane.recognition.Language)
	 */
	@Override
	public Set<String> getCommands(final Language lang) {
		final Set<String> set = new HashSet<String>();

		if (lang == Language.french) {
			set.add("quel.* météo à .*");
			set.add("quel.* météo au.* .*");
			set.add("quel.* temps fait.*il à .*");
			set.add("quel.* le temps à.*");
		} else {
			set.add("what.* the weather like in .*");
		}

		return set;
	}

	private String getWeatherForecast(final String city, final Language lang) {
		final StringBuilder out = new StringBuilder();

		// Using openweathermap.org api
		String response;
		try {
			response = AsilaneUtils.curl("http://api.openweathermap.org/data/2.5/weather?q="
					+ UrlUtil.encode(city, "UTF-8") + "&lang=" + lang.toString().substring(0, 2));
		} catch (final UnsupportedEncodingException e) {
			return handleErrorMessage(lang);
		}
		if (response == null) {
			return handleErrorMessage(lang);
		}

		// Parsing api response
		final JSONObject parsedResponse = (JSONObject) JSONValue.parse(response);
		// If the city is not found
		if (parsedResponse.get("weather") == null) {
			if (lang == Language.french) {
				return "La ville \"" + city + "\" n'a pas pu être trouvée.";
			}
			return "The city \"" + city + "\" could not be found";
		}

		// Parse weather proper
		final JSONObject parsedWeather = (JSONObject) JSONValue.parse(parsedResponse.get("weather").toString()
				.substring(1, parsedResponse.get("weather").toString().length() - 1));
		final JSONObject parsedMain = (JSONObject) JSONValue.parse(parsedResponse.get("main").toString());
		final String cleanCity = parsedResponse.get("name").toString().isEmpty() ? city : parsedResponse.get("name")
				.toString();

		// Convert Kelvin temperature to Celsius
		final int temperature = (int) Math.round((double) parsedMain.get("temp") - 273.15);

		// Saying the weather in the appropriate language
		if (lang == Language.french) {
			out.append("Voici le temps à " + cleanCity + " : ");
			out.append(parsedWeather.get("description"));
			out.append(", humidité à " + parsedMain.get("humidity") + "%, et température à " + temperature
					+ " degrés celsius.");
		} else {
			out.append("This is the weather at " + cleanCity + " : ");
			out.append(parsedWeather.get("description"));
			out.append(", " + parsedMain.get("humidity") + "% of humidity, and temperature at " + temperature
					+ " degrees celsius.");
		}

		return out.toString();
	}

	private String handleErrorMessage(final Language lang) {
		if (lang == Language.french) {
			return "Impossible de récupérer la météo.";
		}
		return "Cannot get the weather forecast.";
	}
}