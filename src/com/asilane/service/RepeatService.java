package com.asilane.service;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.asilane.core.AsilaneUtils;
import com.asilane.core.facade.history.HistoryTree;

/**
 * @author walane
 * 
 */
public class RepeatService implements IService {

	private final Set<String> commands = new HashSet<String>();

	private static final String REPETE_APRES_MOI = ".*répète après moi.*";
	private static final String REPEAT_AFTER_ME = ".*repeat after me.*";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Locale)
	 */
	@Override
	public String handleService(final String sentence, final Locale lang, final HistoryTree historyTree) {
		// FRENCH
		if (lang == Locale.FRANCE) {
			return AsilaneUtils.extractRegexVars(REPETE_APRES_MOI, sentence).get(1);
		}

		// ENGLISH
		return AsilaneUtils.extractRegexVars(REPETE_APRES_MOI, sentence).get(1);
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
				commands.add(REPETE_APRES_MOI);
			} else {
				commands.add(REPEAT_AFTER_ME);
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
		return null;
	}
}