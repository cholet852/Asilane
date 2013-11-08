/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.service.AsilaneDialog;

import com.asilane.core.AsilaneUtils;
import com.asilane.core.RegexVarsResult;
import com.asilane.core.facade.Question;
import com.asilane.core.facade.Response;
import com.asilane.core.facade.ServiceDispatcher;
import com.asilane.core.facade.Translator;
import com.asilane.service.IService;

/**
 * @author walane
 * 
 */
public class AsilaneDialogService implements IService {

	private static enum dynamicCommands {
		last_asked_thing, say_hello_to
	}

	private static enum staticCommands {
		yes, thank_you, test, hello, bye, how_are_you, and_you
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleService(com.asilane.core.facade.Question)
	 */
	@Override
	public Response handleService(final Question question) {
		final Translator translator = ServiceDispatcher.getInstance(question.getLanguage()).getTranslation(this);
		RegexVarsResult regexVars = null;

		if (AsilaneUtils.extractRegexVars(translator.getQuestion(dynamicCommands.last_asked_thing), question) != null) {
			final Response lastResponse = question.getHistoryTree().getLastNode().getResponse();
			return (lastResponse == null) ? new Response(translator.getAnswer(dynamicCommands.last_asked_thing)) : lastResponse;
		} else if ((regexVars = AsilaneUtils.extractRegexVars(translator.getQuestion(dynamicCommands.say_hello_to), question)) != null) {
			return new Response(translator.getAnswer(dynamicCommands.say_hello_to, regexVars.get("name")));
		} else if (AsilaneUtils.extractRegexVars(translator.getQuestion(staticCommands.and_you), question) != null) {
			return new Response(translator.getAnswer(staticCommands.how_are_you));
		}

		return AsilaneUtils.handleStaticCommands(staticCommands.values(), question, translator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleRecoveryService(com.asilane.core.facade.Question)
	 */
	@Override
	public Response handleRecoveryService(final Question question) {
		final Translator translator = ServiceDispatcher.getInstance(question.getLanguage()).getTranslation(this);

		if (AsilaneUtils.extractRegexVars(translator.getQuestion(staticCommands.and_you), question) != null) {
			return new Response(translator.getAnswer(staticCommands.how_are_you));
		}

		return null;
	}
}