package com.asilane.service;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import com.asilane.recognition.Language;
import com.sun.jndi.toolkit.url.UrlUtil;

/**
 * @author walane
 * 
 */
public class WebBrowser implements IService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Language)
	 */
	@Override
	public String handleService(final String sentence, final Language lang) {
		if (Desktop.isDesktopSupported()) {
			// TODO : Use regular expressions to extract vars
			// Extract the website we are looking for
			String term = "";
			if (lang == Language.french) {
				if (sentence.contains("sur le")) {
					term = sentence.substring(sentence.lastIndexOf("sur le") + 6).trim();
				} else if (sentence.contains("sur les")) {
					term = sentence.substring(sentence.lastIndexOf("sur les") + 7).trim();
				} else if (sentence.contains("sur la")) {
					term = sentence.substring(sentence.lastIndexOf("sur la") + 6).trim();
				} else {
					term = sentence.substring(sentence.lastIndexOf("sur") + 3).trim();
				}
			} else {
				term = sentence.substring(sentence.lastIndexOf("on") + 2).trim();
			}

			// If no website provided
			if (term.isEmpty()) {
				if (lang == Language.french) {
					return "Merci de spécifier un site Web";
				}
				return "Please specify a website.";
			}

			// Find website, go to this website, say a confirmation
			final Desktop desktop = Desktop.getDesktop();
			if (desktop.isSupported(Desktop.Action.BROWSE)) {
				try {
					// Duck duck go is used to get the website for more anonymous
					desktop.browse(URI.create("https://duckduckgo.com/?q=!%20" + UrlUtil.encode(term, "UTF-8")));

					if (lang == Language.french) {
						return "Ok, je cherche des informations sur " + term + ".";
					}
					return "Ok, i'm looking for informations on " + term + ".";
				} catch (final IOException e) {
					return handleErrorMessage(lang);
				}
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
			set.add("va sur.*");
			set.add("cherche des info.* sur.*");
			set.add("donne.*moi des info.* sur.*");
		} else {
			set.add("go on.*");
			set.add("search info.* on.*");
		}

		return set;
	}
}