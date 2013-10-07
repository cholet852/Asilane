package com.asilane.service.AsilaneDialog;

import com.asilane.core.AsilaneUtils;
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
		last_asked_thing
	}

	private static enum staticCommands {
		yes, thank_you, test, good_bye
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleService(java.lang.String, com.asilane.recognition.Locale)
	 */
	@Override
	public Response handleService(final Question question) {
		final Translator translator = ServiceDispatcher.getInstance(question.getLanguage()).getTranslation(this);

		if (AsilaneUtils.extractRegexVars(translator.getQuestion(dynamicCommands.last_asked_thing), question) != null) {
			final Response lastResponse = question.getHistoryTree().getLastNode().getResponse();
			return (lastResponse == null) ? new Response(translator.getAnswer(dynamicCommands.last_asked_thing))
					: lastResponse;
		}

		return AsilaneUtils.handleStaticCommands(staticCommands.values(), question, translator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleRecoveryService(java.lang.String, com.asilane.core.Locale)
	 */
	@Override
	public Response handleRecoveryService(final Question question) {
		return null;
	}
}