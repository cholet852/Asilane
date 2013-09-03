package com.asilane.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.asilane.core.AsilaneUtils;
import com.asilane.core.facade.history.HistoryTree;

/**
 * @author walane
 * 
 */
public class IPService implements IService {

	private static final String WHAT_IS_MY_IP = "what is my ip.*";
	private static final String QUELLE_EST_MON_IP = "quel.* es.* mon .*ip";

	@Override
	public String handleService(final String sentence, final Locale lang, final HistoryTree historyTree) {
		// External IP
		if (sentence.matches(QUELLE_EST_MON_IP) || sentence.matches(WHAT_IS_MY_IP)) {
			return getExternalIP();
		}

		// Local IP
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (final UnknownHostException e) {
			if (lang == Locale.FRANCE) {
				return "Impossible de trouver votre adresse IP locale.";
			}
			return "Could not find your local IP address.";
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#getCommands(com.asilane.recognition.Locale)
	 */
	@Override
	public Set<String> getCommands(final Locale lang) {
		final Set<String> set = new HashSet<String>();

		if (lang == Locale.FRANCE) {
			set.add(QUELLE_EST_MON_IP);
			set.add("quel.* es.* mon .*ip.*local.*");
		} else {
			set.add(WHAT_IS_MY_IP);
			set.add("what is my local ip.*");
		}

		return set;
	}

	private String getExternalIP() {
		final String response = AsilaneUtils.curl("http://checkip.amazonaws.com");
		if (response == null) {
			return "Cannot find your IP address, are you connected to the Internet ?";
		}
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleRecoveryService(java.lang.String, com.asilane.core.Locale)
	 */
	@Override
	public String handleRecoveryService(final String sentence, final Locale lang) {
		return null;
	}
}