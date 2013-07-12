package com.asilane.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.asilane.recognition.Language;

/**
 * @author walane
 * 
 */
public class DateService implements IService {

	private static final String QUEL_JOUR_SOMME_NOUS = "quel.* jour somme.*nous";
	private static final String ON_EST_QUEL_JOUR = "on.* es.* quel.* jour";
	private static final String QUEL_EST_LA_DATE = "quel.* es.* la date.*";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Language)
	 */
	@Override
	public String handleService(final String sentence, final Language lang) {
		final Date date = new Date();
		final DateFormat dateFormat = new SimpleDateFormat("EEEE d MMMM YYYY");
		final DateFormat timeFormat = new SimpleDateFormat("HH:MM");

		if (lang == Language.french) {
			if (sentence.matches(QUEL_JOUR_SOMME_NOUS) || sentence.matches(ON_EST_QUEL_JOUR)
					|| sentence.matches(QUEL_EST_LA_DATE)) {
				return "Nous sommes le " + dateFormat.format(date);
			}

			return "Il est " + timeFormat.format(date) + ".";
		}

		return "It is " + timeFormat.format(date) + ".";
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
			set.add("quel.* heure es.*il");
			set.add("il es.* quel.* heure.*");

			set.add(QUEL_JOUR_SOMME_NOUS);
			set.add(ON_EST_QUEL_JOUR);
			set.add(QUEL_EST_LA_DATE);
		} else {
			set.add("what.* time is it");
		}

		return set;
	}
}