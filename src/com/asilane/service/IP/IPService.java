package com.asilane.service.IP;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
public class IPService implements IService {

	private static enum staticCommands {
		what_is_my_ip, what_is_my_local_ip, error_local_ip, error_external_ip
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleService(com.asilane.core.facade.Question)
	 */
	@Override
	public Response handleService(final Question question) {
		final Translator translator = ServiceDispatcher.getInstance(question.getLanguage()).getTranslation(this);

		// Local IP
		if (AsilaneUtils.extractRegexVars(translator.getQuestion(staticCommands.what_is_my_local_ip), question) != null) {
			String response;
			try {
				final String localIp = InetAddress.getLocalHost().getHostAddress();
				response = translator.getAnswer(staticCommands.what_is_my_local_ip, localIp);
			} catch (final UnknownHostException e) {
				response = translator.getQuestion(staticCommands.error_local_ip);
			}

			return new Response(response);
		}

		// External IP
		final String externalIP = AsilaneUtils.curl("http://checkip.amazonaws.com");
		if (externalIP == null) {
			return new Response(translator.getQuestion(staticCommands.error_external_ip));
		}

		return new Response(translator.getAnswer(staticCommands.what_is_my_ip, externalIP));
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