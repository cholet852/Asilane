package com.asilane.service.YouTube;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

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
public class YouTubeService implements IService {

	private static enum dynamicCommands {
		video
	}

	private static enum errors {
		error_empty_term, error_desktop
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Locale)
	 */
	@Override
	public Response handleService(final Question question) {
		final Translator translator = ServiceDispatcher.getInstance(question.getLanguage()).getTranslation(this);

		if (Desktop.isDesktopSupported()) {
			// Extract the website we are looking for
			RegexVarsResult regexVars = null;

			if ((regexVars = AsilaneUtils.extractRegexVars(translator.getQuestion(dynamicCommands.video), question)) != null) {
				return new Response(handleSearch(regexVars.get("term"), translator));
			}
		}

		return new Response(translator.getQuestion(errors.error_desktop));
	}

	private String handleSearch(final String term, final Translator translator) {
		if (term.isEmpty()) {
			return translator.getQuestion(errors.error_empty_term);
		}

		// Find website, go to this website, say a confirmation
		final Desktop desktop = Desktop.getDesktop();
		if (desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				// Duck duck go is used to get the website for more anonymous
				desktop.browse(URI.create("https://duckduckgo.com/?q=!%20site:youtube.com%2Fwatch%20" + AsilaneUtils.encode(term)));

				return translator.getAnswer(dynamicCommands.video);
			} catch (final IOException e) {
				return translator.getQuestion(errors.error_desktop);
			}
		}

		return translator.getQuestion(errors.error_desktop);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleRecoveryService(java.lang.String, com.asilane.core.Locale)
	 */
	@Override
	public Response handleRecoveryService(final Question question) {
		return null;
	}
}