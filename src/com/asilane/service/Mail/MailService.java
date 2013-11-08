/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.service.Mail;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

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
public class MailService implements IService {

	private String arobas;
	private String dot;

	private static enum staticCommands {
		send_a_mail
	}

	private static enum dynamicCommands {
		send_a_mail_to, send_a_mail_to_with_body
	}

	private static enum errors {
		error_client_mail, error_send_mail
	}

	private static enum others {
		arobas, dot
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleService(com.asilane.core.facade.Question)
	 */
	@Override
	public Response handleService(final Question question) {
		final Translator translator = ServiceDispatcher.getInstance(question.getLanguage()).getTranslation(this);
		arobas = translator.getQuestion(others.arobas);
		dot = translator.getQuestion(others.dot);

		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
			try {
				RegexVarsResult regexVars = null;

				// Mail with dest & body
				if ((regexVars = AsilaneUtils.extractRegexVars(translator.getQuestion(dynamicCommands.send_a_mail_to_with_body), question)) != null) {
					final String dest = textToEmailAddress(regexVars.get("mail"));
					mail(dest, "", regexVars.get("body"));

					return new Response(translator.getAnswer(dynamicCommands.send_a_mail_to_with_body, dest));
				}

				// Mail with dest only
				else if ((regexVars = AsilaneUtils.extractRegexVars(translator.getQuestion(dynamicCommands.send_a_mail_to), question)) != null) {
					final String dest = textToEmailAddress(regexVars.get("mail"));
					mail(dest, "", "");

					return new Response(translator.getAnswer(dynamicCommands.send_a_mail_to, dest));
				}

				// Blank mail
				mail("", "", "");
				return new Response(translator.getAnswer(staticCommands.send_a_mail));

			} catch (final IOException e) {
				return new Response(translator.getQuestion(errors.error_send_mail));
			}
		}

		return new Response(translator.getQuestion(errors.error_client_mail));
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

	/**
	 * Transform a text into a valid email address
	 * 
	 * @param text
	 * @return the valid email address corresponding to the text
	 */
	private String textToEmailAddress(final String text) {
		return text.replace(arobas, "@").replace(dot, ".").replace(" ", "");
	}

	/**
	 * Send a mail
	 * 
	 * @param dest
	 * @param subject
	 * @param message
	 * @throws IOException
	 */
	private void mail(final String dest, final String subject, final String message) throws IOException {
		Desktop.getDesktop().mail(
				URI.create("mailto:" + dest + "?subject=" + AsilaneUtils.encode(subject) + "&body=" + AsilaneUtils.encode(message)));
	}

}