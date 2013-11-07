package com.asilane.service.FindPlace;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.Locale;

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
public class FindPlaceService implements IService {

	private static enum dynamicCommands {
		where_is, itinerary, and
	}

	private static enum staticCommands {
		cannot_open_web_browser
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleService(com.asilane.core.facade.Question)
	 */
	@Override
	public Response handleService(final Question question) {
		if (Desktop.isDesktopSupported()) {
			RegexVarsResult regexVars = null;
			final Translator translator = ServiceDispatcher.getInstance(question.getLanguage()).getTranslation(this);

			if ((regexVars = AsilaneUtils.extractRegexVars(translator.getQuestion(dynamicCommands.where_is), question)) != null) {
				return new Response(handleSearch(regexVars.get("place"), question.getLanguage()));
			} else if ((regexVars = AsilaneUtils.extractRegexVars(translator.getQuestion(dynamicCommands.itinerary), question)) != null) {
				return new Response(handleSearch("from:" + regexVars.get("place1") + " to:" + regexVars.get("place2"),
						question.getLanguage()));
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleRecoveryService(com.asilane.core.facade.Question)
	 */
	@Override
	public Response handleRecoveryService(final Question question) {
		RegexVarsResult regexVars = null;
		final Translator translator = ServiceDispatcher.getInstance(question.getLanguage()).getTranslation(this);

		if ((regexVars = AsilaneUtils.extractRegexVars(translator.getQuestion(dynamicCommands.and), question)) != null) {
			return new Response(handleSearch(regexVars.get("place"), question.getLanguage()));
		}

		return null;
	}

	/**
	 * Handle place search
	 * 
	 * @param place
	 * @param lang
	 * @return place location
	 */
	private String handleSearch(final String place, final Locale lang) {
		final Translator translator = ServiceDispatcher.getInstance(lang).getTranslation(this);

		// Find website, go to this website, say a confirmation
		final Desktop desktop = Desktop.getDesktop();
		if (desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				final String langParsed = lang.getCountry().isEmpty() ? lang.toString() : lang.getCountry();
				desktop.browse(URI.create("https://maps.google.com/maps?q=" + AsilaneUtils.encode(place) + "&hl=" + langParsed));

				if (place.contains("from")) {
					return translator.getAnswer(dynamicCommands.itinerary);
				} else {
					return translator.getAnswer(dynamicCommands.where_is, place);
				}
			} catch (final IOException e) {
				return translator.getAnswer(staticCommands.cannot_open_web_browser);
			}
		}

		return translator.getAnswer(staticCommands.cannot_open_web_browser);
	}
}