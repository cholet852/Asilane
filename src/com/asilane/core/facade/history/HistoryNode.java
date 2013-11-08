/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.core.facade.history;

import com.asilane.core.facade.Question;
import com.asilane.core.facade.Response;
import com.asilane.service.IService;

/**
 * A node of the history tree which save every question asked during the session
 * 
 * @author walane
 */
public class HistoryNode {
	/**
	 * The sentence asked by the client
	 */
	private Question question;
	/**
	 * The answer given by the IA
	 */
	private Response response;
	/**
	 * The service which has managed the sentence to give the answer
	 */
	private IService service;

	/**
	 * The next node is at left if the next question asked have a link to this question
	 */
	private HistoryNode leftSon;
	/**
	 * The next node is at right if the next question asked have no any link to this question
	 */
	private HistoryNode rightSon;

	/**
	 * 
	 */
	public HistoryNode() {
	}

	/**
	 * @param sentence
	 *            The sentence asked by the client
	 * @param answer
	 *            The answer given by the IA
	 * @param service
	 *            The service which has managed the sentence to give the answer
	 */
	public HistoryNode(final Question question, final Response response, final IService service) {
		this.question = question;
		this.response = response;
		this.service = service;
	}

	/**
	 * @return the question asked by the client
	 */
	public Question getQuestion() {
		return question;
	}

	/**
	 * @param sentence
	 *            the sentence to set
	 */
	public void setQuestion(final Question question) {
		this.question = question;
	}

	/**
	 * @return the response given by the IA
	 */
	public Response getResponse() {
		return response;
	}

	/**
	 * @param response
	 *            the answer to set
	 */
	public void setResponse(final Response response) {
		this.response = response;
	}

	/**
	 * @return the service which has managed the sentence to give the answer
	 */
	public IService getService() {
		return service;
	}

	/**
	 * @param service
	 *            the service to set
	 */
	public void setService(final IService service) {
		this.service = service;
	}

	/**
	 * @return the leftSon if the next question asked have a link to the question
	 */
	public HistoryNode getLeftSon() {
		return leftSon;
	}

	/**
	 * @param leftSon
	 *            the leftSon to set
	 */
	public void setLeftSon(final HistoryNode leftSon) {
		this.leftSon = leftSon;
	}

	/**
	 * @return the rightSon if the next question asked have no any link to the question
	 */
	public HistoryNode getRightSon() {
		return rightSon;
	}

	/**
	 * @param rightSon
	 *            the rightSon to set
	 */
	public void setRightSon(final HistoryNode rightSon) {
		this.rightSon = rightSon;
	}

	/**
	 * A node is a leaf if it has no any son
	 * 
	 * @return true if the node is a leaf, false not
	 */
	public boolean isLeaf() {
		return (rightSon == null && leftSon == null);
	}
}