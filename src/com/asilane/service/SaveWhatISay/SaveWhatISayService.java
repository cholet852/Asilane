package com.asilane.service.SaveWhatISay;

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
public class SaveWhatISayService implements IService {

	private static enum dynamicCommands {
		save_what_i_say
	}

	private static enum errors {
		error_empty_sentence
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Locale)
	 */
	@Override
	public Response handleService(final Question question) {
		final Translator translator = ServiceDispatcher.getInstance(question.getLanguage()).getTranslation(this);
		final RegexVarsResult regexVars = AsilaneUtils.extractRegexVars(translator.getQuestion(dynamicCommands.save_what_i_say), question);
		final String sentence = regexVars.get("sentence");

		if (sentence == null || sentence.isEmpty()) {
			return new Response(translator.getQuestion(errors.error_empty_sentence));
		}

		AsilaneUtils.setClipboardContents(sentence.trim());
		return new Response(translator.getAnswer(dynamicCommands.save_what_i_say));
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