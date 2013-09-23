package com.asilane.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.asilane.core.facade.history.HistoryTree;

/**
 * @author walane
 * 
 */
public class DateService implements IService {

	private final Set<String> commands = new HashSet<String>();

	private static final String QUEL_JOUR_SOMME_NOUS = "quel.* jour somme.*nous";
	private static final String ON_EST_QUEL_JOUR = "on.* es.* quel.* jour";
	private static final String QUEL_JOUR_ON_EST = "quel.* jour on.* es.*";
	private static final String QUEL_EST_LA_DATE = "quel.* es.* la date.*";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Locale)
	 */
	@Override
	public String handleService(final String sentence, final Locale lang, final HistoryTree historyTree) {
		final Date date = new Date();
		final DateFormat dateFormat = new SimpleDateFormat("EEEE d MMMM YYYY");
		final DateFormat timeFormat = new SimpleDateFormat("HH:mm");

		if (lang == Locale.FRANCE) {
			if (sentence.matches(QUEL_JOUR_SOMME_NOUS) || sentence.matches(ON_EST_QUEL_JOUR)
					|| sentence.matches(QUEL_EST_LA_DATE) || sentence.matches(QUEL_JOUR_ON_EST)) {
				return "Nous sommes le " + dateFormat.format(date) + ".";
			}

			return "Il est " + timeFormat.format(date) + ".";
		}

		return "It is " + timeFormat.format(date) + ".";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#getCommands(com.asilane.recognition.Locale)
	 */
	@Override
	public Set<String> getCommands(final Locale lang) {
		if (commands.isEmpty()) {

			if (lang == Locale.FRANCE) {
				commands.add("quel.* heure es.*il");
				commands.add("il es.* quel.* heure.*");

				commands.add(QUEL_JOUR_SOMME_NOUS);
				commands.add(ON_EST_QUEL_JOUR);
				commands.add(QUEL_JOUR_ON_EST);
				commands.add(QUEL_EST_LA_DATE);
			} else {
				commands.add("what.* time is it");
			}
		}
		return commands;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleRecoveryService(java.lang.String, com.asilane.core.Locale)
	 */
	@Override
	public String handleRecoveryService(final String sentence, final Locale lang, final HistoryTree historyTree) {
		if (lang == Locale.FRANCE) {
			if (sentence.contains("et maintenant")) {
				return handleService("", lang, null);
			}
		}
		if (sentence.contains("and now")) {
			return handleService("", lang, null);
		}

		return null;
	}
}