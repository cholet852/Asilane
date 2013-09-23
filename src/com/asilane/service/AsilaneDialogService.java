package com.asilane.service;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.asilane.core.facade.ServiceDispatcher;
import com.asilane.core.facade.history.HistoryNode;
import com.asilane.core.facade.history.HistoryTree;

/**
 * @author walane
 * 
 */
public class AsilaneDialogService implements IService {

	private final Set<String> commands = new HashSet<String>();

	private static final String DERNIERE_CHOSE_DEMANDE = ".*dernière chose .* demandé";
	private static final String AU_REVOIR = "au.*revoir";
	private static final String TEST = "test";
	private static final String NO = "no";
	private static final String YES = "yes";
	private static final String QUE_SAIS_TU_FAIRE = ".*sais.* faire.*";
	private static final String MERCI = ".*merci";
	private static final String OUI = "oui";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Locale)
	 */
	@Override
	public String handleService(final String sentence, final Locale lang, final HistoryTree historyTree) {
		if (sentence.matches(OUI)) {
			return "Ok.";

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
	 * @see com.asilane.service.IService#getCommands(com.asilane.recognition.Locale)
	 */
	@Override
	public Set<String> getCommands(final Locale lang) {
		if (commands.isEmpty()) {
			if (lang == Locale.FRANCE) {
				commands.add(OUI);
				commands.add(MERCI);
				commands.add(QUE_SAIS_TU_FAIRE);
				commands.add(TEST);
				commands.add(AU_REVOIR);
				commands.add(DERNIERE_CHOSE_DEMANDE);
			} else {
				commands.add(YES);
				commands.add(NO);
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