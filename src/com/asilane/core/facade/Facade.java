package com.asilane.core.facade;

import com.asilane.core.facade.history.HistoryNode;
import com.asilane.core.facade.history.HistoryTree;
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
	 * @param question
	 * 
	 * @return the response
	 * @throws NoServiceFoundException
	 */
	public Response handleSentence(final Question question) throws NoServiceFoundException {
		// Preparation of sentence
		question.cleanQuestion();

		// Try to get the service corresponding to the sentence
		final IService askedService = ServiceDispatcher.getInstance(question.getLanguage()).getService(
				question.getQuestion());

		// Return the response of the service if this one is found
		if (askedService != null) {
			final Response response = askedService.handleService(question);
			historyTree.addNode(question, response, askedService);
			return response;
		}

		// If no any service has been found trying to call recovery handling in the previous service
		final HistoryNode lastNode = historyTree.getLastNode();
		final Response recoveryResponse = historyTree.getFirstNode().isLeaf() ? null : lastNode.getService()
				.handleRecoveryService(question);
		if (recoveryResponse != null) {
			historyTree.addNode(question, recoveryResponse, lastNode.getService());
			return recoveryResponse;
		}

		// If normal handling and recovery hangling don't work, exception
		throw new NoServiceFoundException(question.getLanguage());
	}

	/**
	 * @return the historyTree
	 */
	public HistoryTree getHistoryTree() {
		return historyTree;
	}
}