package com.asilane.service;

import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.asilane.core.AsilaneUtils;
import com.asilane.core.Language;
import com.asilane.core.facade.history.HistoryTree;
import com.sun.jndi.toolkit.url.UrlUtil;

/**
 * @author walane
 * 
 */
public class WikipediaService implements IService {

	private static final String WHAT_IS_A = "what.* .*";
	private static final String QU_EST_CE_QUE = "qu.*ce.*que.* .*";
	private static final String QU_EST_CE_QU = "qu.*ce.*qu'.* .*";
	private static final String CEST_QUOI = "c'est quoi.* .*";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Language)
	 */
	@Override
	public String handleService(final String sentence, final Language lang, final HistoryTree historyTree) {
		List<String> regexVars = null;
		String wikipediaResult = null;

		// FRENCH
		if (lang == Language.french) {
			if ((regexVars = AsilaneUtils.extractRegexVars(QU_EST_CE_QU, sentence)) != null) {
				wikipediaResult = getInfosFromWikipedia(regexVars.get(3), lang);
			} else if ((regexVars = AsilaneUtils.extractRegexVars(QU_EST_CE_QUE, sentence)) != null) {
				final String var = (regexVars.get(1) == null || regexVars.get(3).isEmpty()) ? regexVars.get(2)
						: regexVars.get(3);
				wikipediaResult = getInfosFromWikipedia(var, lang);
			} else if ((regexVars = AsilaneUtils.extractRegexVars(CEST_QUOI, sentence)) != null) {
				final String var = (regexVars.get(1) == null || regexVars.get(1).isEmpty()) ? regexVars.get(0)
						: regexVars.get(1);
				wikipediaResult = getInfosFromWikipedia(var, lang);
			}

			if (wikipediaResult == null) {
				if (!AsilaneUtils.isConnectedToInternet()) {
					return "Désolé mais j'ai besoin d'Internet pour savoir ce que cela signifie.";
				}
				return "Désolé mais je ne connais pas ce mot.";
			}
			return wikipediaResult;
		}

		// ENGLISH
		if ((regexVars = AsilaneUtils.extractRegexVars(WHAT_IS_A, sentence)) != null) {
			return getInfosFromWikipedia(regexVars.get(1), lang);
		}
		return null;
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
			set.add(QU_EST_CE_QUE);
			set.add(QU_EST_CE_QU);
			set.add(CEST_QUOI);
		} else {
			set.add(WHAT_IS_A);
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
			if ((regexVars = AsilaneUtils.extractRegexVars("et un.* .*", sentence)) != null) {
				final String var = (regexVars.get(1) == null || regexVars.get(1).isEmpty()) ? regexVars.get(0)
						: regexVars.get(1);
				return getInfosFromWikipedia(var, lang);
			}
		}

		// ENGLISH
		if ((regexVars = AsilaneUtils.extractRegexVars("and.* .*", sentence)) != null) {
			final String var = (regexVars.get(1) == null || regexVars.get(1).isEmpty()) ? regexVars.get(0) : regexVars
					.get(1);
			return getInfosFromWikipedia(var, lang);
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
	private String getInfosFromWikipedia(final String info, final Language lang) {
		try {
			final String ipService = "http://" + lang.toString().substring(0, 2)
					+ ".wikipedia.org/w/api.php?action=opensearch&search=" + UrlUtil.encode(info.trim(), "UTF-8")
					+ "&format=xml&limit=1";
			final String xmlResponse = AsilaneUtils.curl(ipService);

			final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			final Document doc = dBuilder.parse(new InputSource(new StringReader(xmlResponse)));
			doc.normalize();

			final NodeList list = doc.getDocumentElement().getElementsByTagName("Item");
			return ((Element) list.item(0)).getElementsByTagName("Description").item(0).getTextContent();

		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}