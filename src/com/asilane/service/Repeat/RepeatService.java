package com.asilane.service.Repeat;

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
public class RepeatService implements IService {

	private static enum dynamicCommands {
		repeat_after_me
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleService(com.asilane.core.facade.Question)
	 */
	@Override
	public Response handleService(final Question question) {
		final Translator translator = ServiceDispatcher.getInstance(question.getLanguage()).getTranslation(this);
		final RegexVarsResult regexVars = AsilaneUtils.extractRegexVars(translator.getQuestion(dynamicCommands.repeat_after_me), question);
		final String sentence = regexVars.get("sentence");

		if (sentence == null || sentence.isEmpty()) {
			return new Response(translator.getAnswer(dynamicCommands.repeat_after_me));
		}

		return new Response(regexVars.get("sentence").trim());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleRecoveryService(com.asilane.core.facade.Question)
	 */
	@Override
	public Response handleRecoveryService(final Question question) {
		return null;
	}
}