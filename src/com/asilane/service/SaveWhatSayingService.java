package com.asilane.service;

import java.util.HashSet;
import java.util.Set;

import com.asilane.core.AsilaneUtils;
import com.asilane.core.Language;
import com.asilane.core.facade.history.HistoryTree;

/**
 * @author walane
 * 
 */
public class SaveWhatSayingService implements IService {

	private static final String ENREGISTRE_CE_QUE_JE_DIS = ".*registr. ce que je dis.*";
	private static final String SAVE_WHAT_I_SAY = ".*save what i say.*";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Language)
	 */
	@Override
	public String handleService(final String sentence, final Language lang, final HistoryTree historyTree) {
		// FRENCH
		if (lang == Language.french) {
			AsilaneUtils.setClipboardContents(AsilaneUtils.extractRegexVars(ENREGISTRE_CE_QUE_JE_DIS, sentence).get(1));
			return "Voilà, j'ai enregistré ce que vous avez dit dans le presse-papier.";
		}

		// ENGLISH
		AsilaneUtils.setClipboardContents(AsilaneUtils.extractRegexVars(SAVE_WHAT_I_SAY, sentence).get(1));
		return "It's done, i have save it in the clipboard.";
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
			set.add(ENREGISTRE_CE_QUE_JE_DIS);
		} else {
			set.add(SAVE_WHAT_I_SAY);
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