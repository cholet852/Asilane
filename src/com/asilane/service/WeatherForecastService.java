package com.asilane.service;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.asilane.core.AsilaneUtils;
import com.asilane.core.facade.history.HistoryTree;

/**
 * @author walane
 * 
 */
public class WeatherForecastService implements IService {

	private final Set<String> commands = new HashSet<String>();

	private static final String WHAT_THE_WEATHER_LIKE_IN = "what.* the weather like .. .*";
	private static final String QUEL_LE_TEMPS_A = "quel.* le temps à.*";
	private static final String QUEL_TEMPS_FAIT_IL_A = "quel.* temps fait.*il à .*";
	private static final String QUEL_METEO_AU = ".*météo au. .*";
	private static final String QUEL_METEO_A = ".*météo à .*";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Locale)
	 */
	@Override
	public String handleService(final String sentence, final Locale lang, final HistoryTree historyTree) {
		String city = "";
		List<String> regexVars = null;

		// FRENCH
		if (lang == Locale.FRANCE) {
			if ((regexVars = AsilaneUtils.extractRegexVars(QUEL_LE_TEMPS_A, sentence)) != null) {
				city = regexVars.get(1);
			} else if ((regexVars = AsilaneUtils.extractRegexVars(QUEL_TEMPS_FAIT_IL_A, sentence)) != null) {
				city = regexVars.get(2);
			} else if ((regexVars = AsilaneUtils.extractRegexVars(QUEL_METEO_AU, sentence)) != null) {
				city = regexVars.get(1);
			} else if ((regexVars = AsilaneUtils.extractRegexVars(QUEL_METEO_A, sentence)) != null) {
				city = regexVars.get(1);
			}
		}
		// ENGLISH
		else {
			regexVars = AsilaneUtils.extractRegexVars(WHAT_THE_WEATHER_LIKE_IN, sentence);
			city = regexVars.get(1);
		}

		// The city must no be empty to get the weather forecast
		if (city.isEmpty()) {
			if (lang == Locale.FRANCE) {
				return "Veuillez spécifier une ville.";
			}
			return "Please specify a city";
		}

		return getWeatherForecast(city, lang);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#getCommands(com.asilane.recognition.Locale)
	 */
	@Override
	public Set<String> getCommands(final Locale lang) {
		if (commands.isEmpty()) {
			if (lang == Locale.FRANCE) {
				commands.add(QUEL_METEO_A);
				commands.add(QUEL_METEO_AU);
				commands.add(QUEL_TEMPS_FAIT_IL_A);
				commands.add(QUEL_LE_TEMPS_A);
				commands.add(QUEL_METEO_A);
			} else {
				commands.add(WHAT_THE_WEATHER_LIKE_IN);
			}
		}
		return commands;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleRecoveryService(java.lang.String, com.asilane.core.Locale)
	 */
	@Override
	public String handleRecoveryService(final String sentence, final Locale lang, final HistoryTree historyTree) {
		List<String> regexVars = null;

		if (lang == Locale.FRANCE) {
			if ((regexVars = AsilaneUtils.extractRegexVars("et .* .*", sentence)) != null) {
				return getWeatherForecast(regexVars.get(1), lang);
			}
		}

		return null;
	}

	private String getWeatherForecast(final String city, final Locale lang) {
		final StringBuilder out = new StringBuilder();

		// Using openweathermap.org api
		String response;
		try {
			response = AsilaneUtils.curl("http://api.openweathermap.org/data/2.5/weather?q="
					+ AsilaneUtils.encode(city) + "&lang=" + lang.toString().substring(0, 2));
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
			if (lang == Locale.FRANCE) {
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
		final int temperature = (int) Math.round(Double.valueOf(parsedMain.get("temp").toString()) - 273.15);

		// Saying the weather in the appropriate Locale
		if (lang == Locale.FRANCE) {
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

	private String handleErrorMessage(final Locale lang) {
		if (lang == Locale.FRANCE) {
			return "Impossible de récupérer la météo.";
		}
		return "Cannot get the weather forecast.";
	}
}