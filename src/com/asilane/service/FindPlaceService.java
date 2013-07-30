package com.asilane.service;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.asilane.core.AsilaneUtils;
import com.asilane.core.Language;
import com.asilane.core.facade.history.HistoryTree;
import com.sun.jndi.toolkit.url.UrlUtil;

/**
 * @author walane
 * 
 */
public class FindPlaceService implements IService {

	private static final String OU_SE_TROUVE = "o. se trouve .*";
	private static final String OU_SE_SITUE = "o. se situe .*";
	private static final String WHERE_IS = "where is .*";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Language)
	 */
	@Override
	public String handleService(final String sentence, final Language lang, final HistoryTree historyTree) {
		if (Desktop.isDesktopSupported()) {
			// Extract the place we are looking for
			List<String> regexVars = null;
			String place = "";

			// FRENCH
			if (lang == Language.french) {
				if ((regexVars = AsilaneUtils.extractRegexVars(OU_SE_TROUVE, sentence)) != null
						|| (regexVars = AsilaneUtils.extractRegexVars(OU_SE_SITUE, sentence)) != null) {
					place = handleSearch(regexVars.get(0), lang);
				}
			}

			// ENGLISH
			if ((regexVars = AsilaneUtils.extractRegexVars(WHERE_IS, sentence)) != null) {
				place = handleSearch(regexVars.get(0), lang);
			}

			// If no website provided
			if (place.isEmpty()) {
				if (lang == Language.french) {
					return "Merci de spécifier un lieu.";
				}
				return "Please specify a place.";
			}
			return place;
		}

		return handleErrorMessage(lang);
	}

	private String handleSearch(final String place, final Language lang) {
		// Find website, go to this website, say a confirmation
		final Desktop desktop = Desktop.getDesktop();
		if (desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				// Duck duck go is used to get the website for more anonymous
				desktop.browse(URI.create("https://maps.google.com/maps?q=" + UrlUtil.encode(place, "UTF-8") + "&hl="
						+ lang.toString().substring(0, 2)));

				if (lang == Language.french) {
					return "Voici où se situe " + place + ".";
				}

				return "This is where " + place + " is.";
			} catch (final IOException e) {
				return handleErrorMessage(lang);
			}
		}
		return handleErrorMessage(lang);
	}

	private String handleErrorMessage(final Language lang) {
		if (lang == Language.french) {
			return "Impossible d'ouvrir votre navigateur Web.";
		}
		return "Cannot open your Web Browser";
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
			set.add(OU_SE_TROUVE);
			set.add(OU_SE_SITUE);
		} else {
			set.add(WHERE_IS);
		}

		return set;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleRecoveryService(java.lang.String, com.asilane.core.Language)
	 */
	@Override
	public String handleRecoveryService(final String sentence, final Language lang) {
		List<String> regexVars = null;

		// FRENCH
		if (lang == Language.french) {
			if ((regexVars = AsilaneUtils.extractRegexVars("et .*", sentence)) != null) {
				return handleSearch(regexVars.get(0), lang);
			}
		}

		// ENGLISH
		if ((regexVars = AsilaneUtils.extractRegexVars("and .*", sentence)) != null) {
			return handleSearch(regexVars.get(0), lang);
		}

		return null;
	}
}