/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.core.facade;

/**
 * This object represent the response of the Asilane IA from a service
 * 
 * @author walane
 * 
 */
public class Response {
	/**
	 * the response to be speeched
	 */
	private String speechedResponse;
	/**
	 * the response to display
	 */
	private String displayedResponse;

	/**
	 * 
	 */
	public Response() {

	}

	/**
	 * Create a new Response and set the speeched response and the displayed response
	 * 
	 * @param response
	 */
	public Response(final String response) {
		this.speechedResponse = response;
		this.displayedResponse = response;
	}

	/**
	 * @param speechedResponse
	 *            : the response to be speeched
	 * @param displayedResponse
	 *            : the response to display
	 */
	public Response(final String speechedResponse, final String displayedResponse) {
		this.speechedResponse = speechedResponse;
		this.displayedResponse = displayedResponse;
	}

	/**
	 * @return the speechedResponse : the response to be speeched
	 */
	public String getSpeechedResponse() {
		return speechedResponse;
	}

	/**
	 * @param speechedResponse
	 *            the speechedResponse to set
	 */
	public void setSpeechedResponse(final String speechedResponse) {
		this.speechedResponse = speechedResponse;
	}

	/**
	 * @return the displayedResponse : the response to display
	 */
	public String getDisplayedResponse() {
		return displayedResponse;
	}

	/**
	 * @param displayedResponse
	 *            the displayedResponse to set
	 */
	public void setDisplayedResponse(final String displayedResponse) {
		this.displayedResponse = displayedResponse;
	}

	/**
	 * Set the speeched response and the displayed response
	 * 
	 * @param response
	 */
	public void setResponse(final String response) {
		this.speechedResponse = response;
		this.displayedResponse = response;
	}
}