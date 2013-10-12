package com.asilane.service.Insult;

import java.util.Random;

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
public class InsultService implements IService {

	private static enum staticCommands {
		family, shud_up, insults, insults_masked, response_
	}

	private static enum dynamicCommands {
		family_keep_calm, no
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Locale)
	 */
	@Override
	public Response handleService(final Question question) {
		final Translator translator = ServiceDispatcher.getInstance(question.getLanguage()).getTranslation(this);

		if (AsilaneUtils.extractRegexVars(translator.getQuestion(staticCommands.family), question) != null) {
			return new Response(translator.getAnswer(staticCommands.family));
		}

		// Return a random response
		final int random = new Random().nextInt(11) + 1;
		final Response res = new Response(translator.getAnswer(staticCommands.response_.toString() + random));

		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleRecoveryService(java.lang.String, com.asilane.core.Locale)
	 */
	@Override
	public Response handleRecoveryService(final Question question) {
		final Translator translator = ServiceDispatcher.getInstance(question.getLanguage()).getTranslation(this);
		final HistoryNode lastNode = question.getHistoryTree().getLastNode();

		if (AsilaneUtils.extractRegexVars(translator.getQuestion(staticCommands.family), lastNode.getQuestion()) != null
				&& AsilaneUtils.extractRegexVars(translator.getQuestion(dynamicCommands.no), question) != null) {
			return new Response(translator.getAnswer(dynamicCommands.family_keep_calm));
		}

		return null;
	}
}