package com.asilane.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import com.asilane.core.facade.history.HistoryNode;
import com.asilane.core.facade.history.HistoryTree;

/**
 * @author walane
 * 
 */
public class InsultService implements IService {

	private static final String TA_MERE_TA_SOEUR = ".*(ta mère|ta soeur).*";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Locale)
	 */
	@Override
	public String handleService(final String sentence, final Locale lang, final HistoryTree historyTree) {
		if (sentence.matches(TA_MERE_TA_SOEUR)) {
			return "Laissons la famille en dehors de ça si vous le voulez bien.";
		}

		final List<String> insultResponseList = new ArrayList<String>();
		insultResponseList.add("Merci.");
		insultResponseList.add("Merci, vous aussi.");
		insultResponseList.add("Ce n'est pas très gentil.");
		insultResponseList.add("Oooohh ! Vous êtes grossier.");
		insultResponseList.add("Merci, c'est toujours sympa de se faire insulter en travaillant...");
		insultResponseList.add("Vous avez l'air très sympatique, c'est agréable.");
		insultResponseList
				.add("Je vois votre tête avec l'appareil photo et il y a aussi de quoi vous insulter croyez-moi...");

		final Random random = new Random();
		return insultResponseList.get(random.nextInt(insultResponseList.size()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#getCommands(com.asilane.recognition.Locale)
	 */
	@Override
	public Set<String> getCommands(final Locale lang) {
		final Set<String> set = new HashSet<String>();

		if (lang == Locale.FRANCE) {
			set.add(".*ta gueule.*");
			set.add(TA_MERE_TA_SOEUR);
			set.add(".*(connard|con|pute|salo|salop|salopard|enfoiré|enculé|bâtard|batard).*");
			set.add(".*(c\\*.*|p\\*.*|s\\*.*|e\\*.*|e\\*.*|e\\*.*|d\\*.*).*");
		} else {
		}

		return set;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleRecoveryService(java.lang.String, com.asilane.core.Locale)
	 */
	@Override
	public String handleRecoveryService(final String sentence, final Locale lang, final HistoryTree historyTree) {
		final HistoryNode lastNode = historyTree.getLastNode();
		if (lastNode.getSentence().matches(TA_MERE_TA_SOEUR) && sentence.contains("non")) {
			return "Alors je vous prie de bien vouloir vous calmer et de revenir lorsque vous serez calme.";
		}

		return null;
	}
}