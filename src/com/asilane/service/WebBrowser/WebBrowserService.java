/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.service.WebBrowser;

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
public class WebBrowserService implements IService {

	private static enum dynamicCommands {
		go_on, search
	}

	private static enum errors {
		error_empty_term, error_desktop
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleService(com.asilane.core.facade.Question)
	 */
	@Override
	public Response handleService(final Question question) {
		final Translator translator = ServiceDispatcher.getInstance(question.getLanguage()).getTranslation(this);

		if (Desktop.isDesktopSupported()) {
			// Extract the website we are looking for
			RegexVarsResult regexVars = null;

			if ((regexVars = AsilaneUtils.extractRegexVars(translator.getQuestion(dynamicCommands.go_on), question)) != null) {
				return new Response(handleSearch(regexVars.get("term"), translator, true));
			} else if ((regexVars = AsilaneUtils.extractRegexVars(translator.getQuestion(dynamicCommands.search), question)) != null) {
				return new Response(handleSearch(regexVars.get("term"), translator, false));
			}
		}

		return null;
	}

	private String handleSearch(final String term, final Translator translator, final boolean directBrowsing) {
		if (term.isEmpty()) {
			return translator.getQuestion(errors.error_empty_term);
		}

		// Find website, go to this website, say a confirmation
		final Desktop desktop = Desktop.getDesktop();
		if (desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				// Duck duck go is used to get the website for more anonymous
				final String directBrowsingString = directBrowsing ? "!%20" : "";
				desktop.browse(URI.create("https://duckduckgo.com/?q=" + directBrowsingString + AsilaneUtils.encode(term)));

				if (directBrowsing) {
					return translator.getAnswer(dynamicCommands.go_on, term);
				}

				return translator.getAnswer(dynamicCommands.search, term);
			} catch (final IOException e) {
				return translator.getQuestion(errors.error_desktop);
			}
		}

		return translator.getQuestion(errors.error_desktop);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleRecoveryService(com.asilane.core.facade.Question)
	 */
	@Override
	public Response handleRecoveryService(final Question question) {
		return null;
	}
}