package com.asilane.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import com.asilane.core.Language;

/**
 * @author walane
 * 
 */
public class IPService implements IService {

	private static final String WHAT_IS_MY_IP = "what is my ip.*";
	private static final String QUELLE_EST_MON_IP = "quel.* es.* mon .*ip";

	@Override
	public String handleService(final String sentence, final Language lang) {
		// External IP
		if (sentence.matches(QUELLE_EST_MON_IP) || sentence.matches(WHAT_IS_MY_IP)) {
			return getExternalIP();
		}

		// Local IP
		try {
			return Inet4Address.getLocalHost().getHostAddress();
		} catch (final UnknownHostException e) {
			if (lang == Language.french) {
				return "Impossible de trouver votre adresse IP locale.";
			}
			return "Could not find your local IP address.";
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#getCommands(com.asilane.recognition.Language)
	 */
	@Override
	public Set<String> getCommands(final Language lang) {
		final Set<String> set = new HashSet<String>();

		if (lang == Language.french) {
			set.add(QUELLE_EST_MON_IP);
			set.add("quel.* es.* mon .*ip.*local.*");
		} else {
			set.add(WHAT_IS_MY_IP);
			set.add("what is my local ip.*");
		}

		return set;
	}

	private String getExternalIP() {
		BufferedReader in = null;
		try {
			// We use amazon to get external IP
			final URL ipService = new URL("http://checkip.amazonaws.com");
			in = new BufferedReader(new InputStreamReader(ipService.openStream()));
			return in.readLine();
		} catch (final Exception e) {
			return "Cannot find your IP address, are you connected to the Internet ?";
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException e) {
					return "Cannot find your IP address, are you connected to the Internet ?";
				}
			}
		}
	}
}