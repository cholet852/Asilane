package com.asilane.service;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.asilane.core.AsilaneUtils;
import com.asilane.core.facade.history.HistoryTree;

/**
 * @author walane
 * 
 */
public class YouTubeService implements IService {

	private static final String VIDEO = "(video|vidéo) .*";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Locale)
	 */
	@Override
	public String handleService(final String sentence, final Locale lang, final HistoryTree historyTree) {
		if (Desktop.isDesktopSupported()) {
			// Extract the website we are looking for
			List<String> regexVars = null;
			final String term = "";

			if ((regexVars = AsilaneUtils.extractRegexVars(VIDEO, sentence)) != null) {
				return handleSearch(regexVars.get(1), lang);
			}

			// If no website provided
			if (term.isEmpty()) {
				if (lang == Locale.FRANCE) {
					return "Merci de spécifier un nom de vidéo.";
				}
				return "Please specify a video name.";
			}
		}

		return handleErrorMessage(lang);
	}

	private String handleSearch(final String term, final Locale lang) {
		// Find website, go to this website, say a confirmation
		final Desktop desktop = Desktop.getDesktop();
		if (desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				// Duck duck go is used to get the website for more anonymous
				desktop.browse(URI.create("https://duckduckgo.com/?q=!%20site:youtube.com%2Fwatch%20"
						+ AsilaneUtils.encode(term)));
				if (lang == Locale.FRANCE) {
					return "C'est parti.";
				} else {
					return "Let's go.";
				}

			} catch (final IOException e) {
				return handleErrorMessage(lang);
			}
		}
		return handleErrorMessage(lang);
	}

	private String handleErrorMessage(final Locale lang) {
		if (lang == Locale.FRANCE) {
			return "Impossible d'ouvrir votre navigateur Web.";
		}
		return "Cannot open your Web Browser.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#getCommands(com.asilane.recognition.Locale)
	 */
	@Override
	public Set<String> getCommands(final Locale lang) {
		final Set<String> set = new HashSet<String>();

		if (lang == Locale.FRANCE) {
			set.add(VIDEO);
		} else {
			set.add(VIDEO);
		}

		return set;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleRecoveryService(java.lang.String, com.asilane.core.Locale)
	 */
	@Override
	public String handleRecoveryService(final String sentence, final Locale lang) {
		return null;
	}
}