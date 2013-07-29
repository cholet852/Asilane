package com.asilane.core.facade.history;

import com.asilane.service.IService;

/**
 * This class save every question asked during the session in binary tree
 * 
 * @author walane
 */
public class HistoryTree {
	private final HistoryNode firstNode;

	/**
	 * Create a history tree to save every question asked during the session in binary tree
	 */
	public HistoryTree() {
		firstNode = new HistoryNode();
	}

	/**
	 * @return the size of the tree
	 */
	public int size() {
		int size = 0;
		HistoryNode tempNode = firstNode;
		while (tempNode != null && !tempNode.isLeaf()) {
			tempNode = (tempNode.getLeftSon() == null) ? tempNode.getRightSon() : tempNode.getLeftSon();
			size++;
		}
		return size;
	}

	/**
	 * @return the first node of the tree
	 */
	public HistoryNode getFirstNode() {
		return firstNode;
	}

	/**
	 * @return the last node of the tree
	 */
	public HistoryNode getLastNode() {
		HistoryNode tempNode = firstNode;
		while (!tempNode.isLeaf()) {
			if (tempNode.isLeaf()) {
				break;
			}
			tempNode = (tempNode.getLeftSon() == null) ? tempNode.getRightSon() : tempNode.getLeftSon();
		}
		return tempNode;
	}

	/**
	 * Add a node to the tree
	 * 
	 * @param sentence
	 * @param answer
	 * @param service
	 */
	public void addNode(final String sentence, final String answer, final IService service) {
		final HistoryNode lastNode = getLastNode();

		// Define if the node will be at left or right (link with the upper node or not)
		if (service.equals(lastNode.getService())) {
			lastNode.setLeftSon(new HistoryNode(sentence, answer, service));
		} else {
			lastNode.setRightSon(new HistoryNode(sentence, answer, service));
		}
	}
}