package com.asilane.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.asilane.facade.AsilaneUtils;
import com.asilane.recognition.Language;
import com.sun.jndi.toolkit.url.UrlUtil;

/**
 * @author walane
 * 
 */
public class WikipediaService implements IService {

	private static final String WHAT_IS_A = "what is a.*";
	private static final String QU_EST_CE_QUE = "qu.*ce.*que .*";
	private static final String QU_EST_CE_QU = "qu.*ce.*qu'.*";
	private static final String QU_EST_CE_QU_UN = "qu.*ce.*qu'un .*";
	private static final String QU_EST_CE_QU_UNE = "qu.*ce.*qu'une .*";
	private static final String CEST_QUOI = "c'est quoi .*";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Language)
	 */
	@Override
	public String handleService(final String sentence, final Language lang) {
		// TODO : Use regular expressions to extract vars
		String info = null;
		if (lang == Language.french) {
			if (sentence.matches(QU_EST_CE_QUE + "le.*")) {
				info = getInfosFromWikipedia(sentence.substring(sentence.lastIndexOf("que le") + 6).trim(), lang);
			} else if (sentence.matches(QU_EST_CE_QUE + "la.*")) {
				info = getInfosFromWikipedia(sentence.substring(sentence.lastIndexOf("que la") + 6).trim(), lang);
			} else if (sentence.matches(QU_EST_CE_QUE)) {
				info = getInfosFromWikipedia(sentence.substring(sentence.lastIndexOf("que") + 3).trim(), lang);
			} else if (sentence.matches(QU_EST_CE_QU_UN)) {
				info = getInfosFromWikipedia(sentence.substring(sentence.lastIndexOf("qu'un") + 5).trim(), lang);
			} else if (sentence.matches(QU_EST_CE_QU_UNE)) {
				info = getInfosFromWikipedia(sentence.substring(sentence.lastIndexOf("qu'une") + 6).trim(), lang);
			} else if (sentence.matches(QU_EST_CE_QU)) {
				info = getInfosFromWikipedia(sentence.substring(sentence.lastIndexOf("qu'") + 3), lang);
			} else if (sentence.matches(CEST_QUOI)) {
				if (sentence.matches("c'est quoi le.*")) {
					info = getInfosFromWikipedia(sentence.substring(sentence.lastIndexOf("le") + 2), lang);
				} else if (sentence.matches("c'est quoi la.*")) {
					info = getInfosFromWikipedia(sentence.substring(sentence.lastIndexOf("la") + 2), lang);
				} else if (sentence.matches("c'est quoi une.*")) {
					info = getInfosFromWikipedia(sentence.substring(sentence.lastIndexOf("une") + 3), lang);
				} else if (sentence.matches("c'est quoi un.*")) {
					info = getInfosFromWikipedia(sentence.substring(sentence.lastIndexOf("un") + 2), lang);
				} else {
					info = getInfosFromWikipedia(sentence.substring(sentence.lastIndexOf("c'est quoi") + 10), lang);
				}
			}
			if (info == null) {
				if (!AsilaneUtils.isConnectedToInternet()) {
					return "Désolé mais j'ai besoin d'Internet pour savoir ce que cela signifie.";
				}
				return "Désolé mais je ne connais pas ce mot.";
			}
			return info;
		}

		if (sentence.matches(WHAT_IS_A)) {
			return getInfosFromWikipedia(sentence.substring(sentence.lastIndexOf("is a") + 4), lang);
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
			set.add(QU_EST_CE_QU_UN);
			set.add(QU_EST_CE_QU_UNE);
			set.add(QU_EST_CE_QU);
			set.add(CEST_QUOI);
		} else {
			set.add(WHAT_IS_A);
		}

		return set;
	}

	private String getInfosFromWikipedia(final String info, final Language lang) {
		final String xmlResponse;
		BufferedReader in = null;
		try {
			// We use amazon to get external IP
			final URL ipService = new URL("http://" + lang.toString().substring(0, 2)
					+ ".wikipedia.org/w/api.php?action=opensearch&search=" + UrlUtil.encode(info, "UTF-8")
					+ "&format=xml&limit=1");
			in = new BufferedReader(new InputStreamReader(ipService.openStream()));
			xmlResponse = in.readLine();
		} catch (final Exception e) {
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException e) {
					return null;
				}
			}
		}

		try {
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