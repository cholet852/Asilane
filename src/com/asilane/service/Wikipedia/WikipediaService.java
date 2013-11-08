/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.service.Wikipedia;

import java.io.StringReader;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

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
public class WikipediaService implements IService {

	private static enum dynamicCommands {
		what_is_a, and
	}

	private static enum errors {
		error_no_internet, error_unknow_term, error_empty_term
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
		String wikipediaResult = null;

		if ((regexVars = AsilaneUtils.extractRegexVars(translator.getQuestion(dynamicCommands.what_is_a), question)) != null) {
			wikipediaResult = getInfosFromWikipedia(regexVars.get("term"), question.getLanguage(), translator);

			if (wikipediaResult == null) {
				if (!AsilaneUtils.isConnectedToInternet()) {
					return new Response(translator.getQuestion(errors.error_no_internet));
				}
				return new Response(translator.getQuestion(errors.error_unknow_term));
			} else if (wikipediaResult.isEmpty()) {
				return new Response(translator.getQuestion(errors.error_empty_term));
			}

			return new Response(wikipediaResult);
		}

		return null;
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

		if ((regexVars = AsilaneUtils.extractRegexVars(translator.getQuestion(dynamicCommands.and), question)) != null) {
			return new Response(getInfosFromWikipedia(regexVars.get("term"), question.getLanguage(), translator));
		}

		return null;
	}

	/**
	 * This will return asked infos from Wikipedia API
	 * 
	 * @param info
	 * @param lang
	 * @return infos from Wikipedia API
	 */
	private String getInfosFromWikipedia(final String info, final Locale lang, final Translator translator) {
		try {
			final String ipService = "http://" + lang.toString().substring(0, 2) + ".wikipedia.org/w/api.php?action=opensearch&search="
					+ AsilaneUtils.encode(info) + "&format=xml&limit=1";
			final String xmlResponse = AsilaneUtils.curl(ipService);

			final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			final Document doc = dBuilder.parse(new InputSource(new StringReader(xmlResponse)));
			doc.normalize();

			final NodeList list = doc.getDocumentElement().getElementsByTagName("Item");
			return ((Element) list.item(0)).getElementsByTagName("Description").item(0).getTextContent();

		} catch (final Exception e) {
			return null;
		}
	}
}