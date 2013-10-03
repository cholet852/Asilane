package com.asilane.service.AsilaneIdentity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.asilane.core.AsilaneUtils;
import com.asilane.core.facade.Question;
import com.asilane.core.facade.Response;
import com.asilane.core.facade.ServiceDispatcher;
import com.asilane.core.facade.Translation;
import com.asilane.service.IService;

/**
 * @author walane
 * 
 */
public class AsilaneIdentityService implements IService {

	private static final String WHO_IS_YOUR_CREATOR = "who_is_your_creator";
	private static final String WHAT_IS_YOUR_GOAL = "what_is_your_goal";
	private static final String HOW_OLD_ARE_YOU = "how_old_are_you";

	private static final int MILISECOND_PER_DAY = 86400000;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleService(java.lang.String, com.asilane.recognition.Locale)
	 */
	@Override
	public Response handleService(final Question question) {
		final Translation translation = ServiceDispatcher.getInstance(question.getLanguage()).getTranslation(this);

		if (AsilaneUtils.extractRegexVars(translation.getQuestion(WHO_IS_YOUR_CREATOR), question) != null) {
			return new Response(translation.getAnswer(WHO_IS_YOUR_CREATOR));
		} else if (AsilaneUtils.extractRegexVars(translation.getQuestion(WHAT_IS_YOUR_GOAL), question) != null) {
			return new Response(translation.getAnswer(WHAT_IS_YOUR_GOAL));
		} else if (AsilaneUtils.extractRegexVars(translation.getQuestion(HOW_OLD_ARE_YOU), question) != null) {
			final GregorianCalendar calendar = new GregorianCalendar();
			calendar.clear();
			calendar.set(2013, Calendar.JULY, 10, 00, 42); // Date of Asilane Creation
			final Date bornDate = calendar.getTime();
			final Date todayDate = new Date();
			final int age = Math.round(Math.abs((todayDate.getTime() - bornDate.getTime()) / MILISECOND_PER_DAY));

			return new Response(translation.getAnswer(HOW_OLD_ARE_YOU, String.valueOf(age)));
		}

		return null;
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