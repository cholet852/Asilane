package com.asilane.service.AsilaneIdentity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
public class AsilaneIdentityService implements IService {

	private static enum dynamicCommands {
		how_old_are_you
	}

	private static enum staticCommands {
		who_is_your_creator, what_is_your_goal
	}

	private static final int MILISECOND_PER_DAY = 86400000;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleService(java.lang.String, com.asilane.recognition.Locale)
	 */
	@Override
	public Response handleService(final Question question) {
		final Translator translator = ServiceDispatcher.getInstance(question.getLanguage()).getTranslation(this);

		if (AsilaneUtils.extractRegexVars(translator.getQuestion(dynamicCommands.how_old_are_you), question) != null) {
			final GregorianCalendar calendar = new GregorianCalendar();
			calendar.set(2013, Calendar.JULY, 10, 00, 42); // Date of Asilane creation
			final Date bornDate = calendar.getTime();
			final Date todayDate = new Date();
			final int age = Math.round(Math.abs((todayDate.getTime() - bornDate.getTime()) / MILISECOND_PER_DAY));

			return new Response(translator.getAnswer(dynamicCommands.how_old_are_you, String.valueOf(age)));
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