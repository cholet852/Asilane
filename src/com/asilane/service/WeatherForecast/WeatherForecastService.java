package com.asilane.service.WeatherForecast;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.asilane.core.AsilaneUtils;
import com.asilane.core.RegexVarsResult;
import com.asilane.core.facade.Question;
import com.asilane.core.facade.Response;
import com.asilane.core.facade.ServiceDispatcher;
import com.asilane.core.facade.Translator;
import com.asilane.service.IService;

/**
 * @author walane
 * 
 */
public class WeatherForecastService implements IService {

	private static enum dynamicCommands {
		what_the_weather_like_in, and_recovery
	}

	private static enum errors {
		error_empty_city, error_incorrect_city, error_retrieve_weather
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Locale)
	 */
	@Override
	public Response handleService(final Question question) {
		final Translator translator = ServiceDispatcher.getInstance(question.getLanguage()).getTranslation(this);
		RegexVarsResult regexVars = null;
		String city = "";

		if ((regexVars = AsilaneUtils.extractRegexVars(translator.getQuestion(dynamicCommands.what_the_weather_like_in), question)) != null) {
			city = regexVars.get("city");
		}

		// The city must no be empty to get the weather forecast
		if (city.isEmpty()) {
			return new Response(translator.getQuestion(errors.error_empty_city));
		}

		return new Response(getWeatherForecast(city, question.getLanguage(), translator));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleRecoveryService(java.lang.String, com.asilane.core.Locale)
	 */
	@Override
	public Response handleRecoveryService(final Question question) {
		final Translator translator = ServiceDispatcher.getInstance(question.getLanguage()).getTranslation(this);
		RegexVarsResult regexVars = null;

		if ((regexVars = AsilaneUtils.extractRegexVars(translator.getQuestion(dynamicCommands.and_recovery), question)) != null) {
			return new Response(getWeatherForecast(regexVars.get("city"), question.getLanguage(), translator));
		}

		return null;
	}

	private String getWeatherForecast(final String city, final Locale lang, final Translator translator) {
		// Using openweathermap.org api
		String response;
		try {
			response = AsilaneUtils.curl("http://api.openweathermap.org/data/2.5/weather?q=" + AsilaneUtils.encode(city) + "&lang="
					+ lang.toString().substring(0, 2));
		} catch (final UnsupportedEncodingException e) {
			return translator.getQuestion(errors.error_retrieve_weather);
		}

		if (response == null) {
			return translator.getQuestion(errors.error_retrieve_weather);
		}

		// Parsing api response
		final JSONObject parsedResponse = (JSONObject) JSONValue.parse(response);

		// If the city is not found
		if (parsedResponse.get("weather") == null) {
			return translator.getQuestion(errors.error_incorrect_city);
		}

		// Parse weather properly
		final JSONObject parsedWeather = (JSONObject) JSONValue.parse(parsedResponse.get("weather").toString()
				.substring(1, parsedResponse.get("weather").toString().length() - 1));
		final JSONObject parsedMain = (JSONObject) JSONValue.parse(parsedResponse.get("main").toString());

		// Set details
		final String cleanCity = parsedResponse.get("name").toString().isEmpty() ? city : parsedResponse.get("name").toString();
		final String weatherDescription = String.valueOf(parsedWeather.get("description"));
		final String weatherHumidity = String.valueOf(parsedMain.get("humidity"));
		// Convert Kelvin temperature to Celsius
		final String temperature = String.valueOf((int) Math.round(Double.valueOf(parsedMain.get("temp").toString()) - 273.15));

		// Return the weather
		return translator.getAnswer(dynamicCommands.what_the_weather_like_in, cleanCity, weatherDescription, weatherHumidity, temperature);
	}
}