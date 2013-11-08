/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.service.Date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.asilane.core.AsilaneUtils;
import com.asilane.core.facade.Question;
import com.asilane.core.facade.Response;
import com.asilane.core.facade.ServiceDispatcher;
import com.asilane.core.facade.Translator;
import com.asilane.core.facade.history.HistoryNode;
import com.asilane.service.IService;

/**
 * @author walane
 * 
 */
public class DateService implements IService {

	private static enum dynamicCommands {
		what_is_the_date, what_time_is_it, and_now
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleService(com.asilane.core.facade.Question)
	 */
	@Override
	public Response handleService(final Question question) {
		final Translator translator = ServiceDispatcher.getInstance(question.getLanguage()).getTranslation(this);

		final Date date = new Date();
		final DateFormat dateFormat = new SimpleDateFormat("EEEE d MMMM YYYY");
		final DateFormat hourFormat = new SimpleDateFormat("HH:mm");

		if (AsilaneUtils.extractRegexVars(translator.getQuestion(dynamicCommands.what_is_the_date), question) != null) {
			return new Response(translator.getAnswer(dynamicCommands.what_is_the_date, dateFormat.format(date)));
		} else if (AsilaneUtils.extractRegexVars(translator.getQuestion(dynamicCommands.what_time_is_it), question) != null) {
			return new Response(translator.getAnswer(dynamicCommands.what_time_is_it, hourFormat.format(date)));
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleRecoveryService(com.asilane.core.facade.Question)
	 */
	@Override
	public Response handleRecoveryService(final Question question) {
		final Translator translator = ServiceDispatcher.getInstance(question.getLanguage()).getTranslation(this);

		if (AsilaneUtils.extractRegexVars(translator.getQuestion(dynamicCommands.and_now), question) != null) {
			// If the last question concern this service, return directly the last answer
			final HistoryNode lastRequest = question.getHistoryTree().getLastNode();
			if (lastRequest.getService().equals(this)) {
				return lastRequest.getResponse();
			}

			// Else, return another answer
			final DateFormat dateFormat = new SimpleDateFormat("EEEE d MMMM YYYY");
			return new Response(translator.getAnswer(dynamicCommands.what_is_the_date, dateFormat.format(new Date())));
		}

		return null;
	}
}