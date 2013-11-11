/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.core.facade;

import java.net.UnknownServiceException;

import com.asilane.core.Asilane;
import com.asilane.core.EnvironmentTools;
import com.asilane.core.IService;
import com.asilane.core.facade.history.HistoryNode;
import com.asilane.core.facade.history.HistoryTree;

/**
 * @author walane
 */
public class Facade {
	/**
	 * The tree which save every question asked during the session in binary tree
	 */
	private final HistoryTree historyTree;

	/**
	 * @see EnvironmentTools
	 */
	private final EnvironmentTools environmentTools;

	public Facade(final EnvironmentTools environmentTools) {
		historyTree = new HistoryTree();
		this.environmentTools = environmentTools;
	}

	/**
	 * Intercept speech to text, prepare the sentence and call the good service
	 * 
	 * @param question
	 * 
	 * @return the response
	 * @throws NoServiceFoundException
	 */
	public Response handleSentence(final Question question) throws UnknownServiceException {
		// Preparation of sentence
		question.cleanQuestion();
		question.setHistoryTree(historyTree);

		// Retrieve the service dispatcher
		final ServiceDispatcher serviceDispatcher = ServiceDispatcher.getInstance(question.getLanguage());
		serviceDispatcher.setEnvironmentTools(environmentTools);

		// Try to get the service corresponding to the sentence
		final IService askedService = serviceDispatcher.getService(question.getQuestion());

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
		throw new UnknownServiceException(Asilane.getConfig().getProperty("error_understanding"));
	}

	/**
	 * @return the historyTree
	 */
	public HistoryTree getHistoryTree() {
		return historyTree;
	}
}