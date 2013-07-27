package com.asilane.facade.history;

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
	private String sentence;
	/**
	 * The answer given by the IA
	 */
	private String answer;
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
	public HistoryNode(final String sentence, final String answer, final IService service) {
		this.sentence = sentence;
		this.answer = answer;
		this.service = service;
	}

	/**
	 * @return the sentence asked by the client
	 */
	public String getSentence() {
		return sentence;
	}

	/**
	 * @param sentence
	 *            the sentence to set
	 */
	public void setSentence(final String sentence) {
		this.sentence = sentence;
	}

	/**
	 * @return the answer given by the IA
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * @param answer
	 *            the answer to set
	 */
	public void setAnswer(final String answer) {
		this.answer = answer;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (isLeaf()) {
			return sentence;
		}
		return "<" + leftSon + ", " + rightSon + ">";
	}
}