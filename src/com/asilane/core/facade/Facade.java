package com.asilane.core.facade;

import com.asilane.core.Language;
import com.asilane.facade.history.HistoryNode;
import com.asilane.facade.history.HistoryTree;
import com.asilane.service.IService;

/**
 * @author walane
 */
public class Facade {
	/**
	 * The tree which save every question asked during the session in binary tree
	 */
	private final HistoryTree historyTree;

	public Facade() {
		historyTree = new HistoryTree();
	}

	/**
	 * Intercept speech to text, prepare the sentence and call the good service
	 * 
	 * @param sentence
	 * @param lang
	 * @return the response
	 */
	public String handleSentence(final String sentence, final Language lang) {
		// Preparation of sentence
		final String preparedSentence = sentence.trim().toLowerCase();

		// Try to get the service corresponding to the sentence
		final IService askedService = ServiceDispatcher.getInstance(lang).getService(preparedSentence);

		// Return the response of the service if this one is found
		if (askedService != null) {
			final String answer = askedService.handleService(preparedSentence, lang, historyTree);
			historyTree.addNode(sentence, answer, askedService);
			return answer;
		}

		// If no any service has been found trying to call recovery handling in the previous service
		final HistoryNode lastNode = historyTree.getLastNode();
		final String recoveryAnswer = historyTree.getFirstNode().isLeaf() ? null : lastNode.getService()
				.handleRecoveryService(preparedSentence, lang);
		if (recoveryAnswer != null) {
			historyTree.addNode(sentence, recoveryAnswer, lastNode.getService());
			return recoveryAnswer;
		}

		// If normal handling and recovery hangling don't work, error message
		if (lang == Language.french) {
			return "Je n'ai pas bien compris, pouvez-vous répéter ?\n" + sentence;
		}
		return "I don't understand, can you repeat?\n" + sentence;
	}
}