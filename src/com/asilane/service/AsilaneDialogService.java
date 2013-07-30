package com.asilane.service;

import java.util.HashSet;
import java.util.Set;

import com.asilane.core.Language;
import com.asilane.core.facade.ServiceDispatcher;
import com.asilane.core.facade.history.HistoryNode;
import com.asilane.core.facade.history.HistoryTree;

/**
 * @author walane
 * 
 */
public class AsilaneDialogService implements IService {

	private static final String DERNIERE_CHOSE_DEMANDE = ".*dernière chose .* demandé";
	private static final String AU_REVOIR = "au.*revoir";
	private static final String TEST = "test";
	private static final String NO = "no";
	private static final String YES = "yes";
	private static final String QUE_SAIS_TU_FAIRE = ".*sais.* faire.*";
	private static final String MERCI = ".*merci";
	private static final String NON = "non";
	private static final String OUI = "oui";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Language)
	 */
	@Override
	public String handleService(final String sentence, final Language lang, final HistoryTree historyTree) {
		if (sentence.matches(OUI)) {
			return "Ok.";
		} else if (sentence.matches(NON)) {
			return "Pourquoi non ?";
		} else if (sentence.matches(MERCI)) {
			return "Derien, c'est un plaisir de vous aider.";
		} else if (sentence.matches(TEST)) {
			return "Ça marche !";
		} else if (sentence.matches(AU_REVOIR)) {
			System.exit(0);
		} else if (sentence.matches(DERNIERE_CHOSE_DEMANDE)) {
			final HistoryNode lastNode = historyTree.getLastNode();
			return lastNode.getService().handleService(lastNode.getSentence(), lang, historyTree);
		} else if (sentence.matches(QUE_SAIS_TU_FAIRE)) {
			final StringBuilder builder = new StringBuilder("Voici tout ce que je sais faire :\n\n");

			// Scan all services commands
			for (final IService service : ServiceDispatcher.getInstance(lang).getAllServices()) {
				builder.append("[" + service.getClass().getSimpleName() + "]\n");
				for (final String regex : service.getCommands(lang)) {
					builder.append(regex + "\n");
				}
				builder.append("\n");
			}
			return builder.toString();
		}

		return "Ok.";
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
			set.add(OUI);
			set.add(NON);
			set.add(MERCI);
			set.add(QUE_SAIS_TU_FAIRE);
			set.add(TEST);
			set.add(AU_REVOIR);
			set.add(DERNIERE_CHOSE_DEMANDE);
		} else {
			set.add(YES);
			set.add(NO);
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