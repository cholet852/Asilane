package com.asilane.service;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.asilane.core.Language;
import com.asilane.facade.AsilaneUtils;
import com.asilane.facade.history.HistoryTree;
import com.sun.jndi.toolkit.url.UrlUtil;

/**
 * @author walane
 * 
 */
public class WebBrowserService implements IService {

	private static final String GO_ON = "go on.*";
	private static final String VA_SUR = "va sur.*";
	private static final String GIVE_ME_INFO_ON = "give me info.* on.*";
	private static final String SEARCH_INFO_ON = "search info.* on.*";
	private static final String INFO_SUR = ".*info.* sur .*";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Language)
	 */
	@Override
	public String handleService(final String sentence, final Language lang, final HistoryTree historyTree) {
		if (Desktop.isDesktopSupported()) {
			// Extract the website we are looking for
			List<String> regexVars = null;
			final String term = "";
			if (lang == Language.french) {
				if ((regexVars = AsilaneUtils.extractRegexVars(VA_SUR, sentence)) != null) {
					return handleSearch(regexVars.get(0), lang, true);
				} else if ((regexVars = AsilaneUtils.extractRegexVars(INFO_SUR, sentence)) != null) {
					return handleSearch(regexVars.get(2), lang, false);
				}
			}
			if ((regexVars = AsilaneUtils.extractRegexVars(GO_ON, sentence)) != null) {
				return handleSearch(regexVars.get(0), lang, true);
			} else if ((regexVars = AsilaneUtils.extractRegexVars(SEARCH_INFO_ON, sentence)) != null) {
				return handleSearch(regexVars.get(1), lang, false);
			}

			// If no website provided
			if (term.isEmpty()) {
				if (lang == Language.french) {
					return "Merci de spécifier un site Web";
				}
				return "Please specify a website.";
			}
		}

		return handleErrorMessage(lang);
	}

	private String handleSearch(final String term, final Language lang, final boolean directBrowsing) {
		// Find website, go to this website, say a confirmation
		final Desktop desktop = Desktop.getDesktop();
		if (desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				// Duck duck go is used to get the website for more anonymous
				final String directBrowsingString = directBrowsing ? "!%20" : "";
				desktop.browse(URI.create("https://duckduckgo.com/?q=" + directBrowsingString
						+ UrlUtil.encode(term, "UTF-8")));

				if (lang == Language.french) {
					return directBrowsing ? "Ok, je vais sur " + term : "Ok, je cherche des informations sur " + term
							+ ".";
				}
				return directBrowsing ? "Ok i'm going on " + term : "Ok, i'm looking for informations on " + term + ".";
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
			set.add(VA_SUR);
			set.add(INFO_SUR);
		} else {
			set.add(GO_ON);
			set.add(SEARCH_INFO_ON);
			set.add(GIVE_ME_INFO_ON);
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
		return null;
	}
}