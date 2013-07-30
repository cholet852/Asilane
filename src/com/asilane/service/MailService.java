package com.asilane.service;

import java.awt.Desktop;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.asilane.core.Language;
import com.asilane.core.facade.history.HistoryTree;

/**
 * @author walane
 * 
 */
public class MailService implements IService {

	private static final String ENVOI_UN_MAIL = "envoi.* un .*mail";
	private static final String SEND_A_MAIL = "send a .*mail";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Language)
	 */
	@Override
	public String handleService(final String sentence, final Language lang, final HistoryTree historyTree) {
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
			try {
				// FRENCH
				if (lang == Language.french) {
					if (sentence.matches(ENVOI_UN_MAIL)) {
						Desktop.getDesktop().mail();
						return "Ok, je vous prépare l'envoi d'un email.";
					}
				}

				// ENGLISH
				if (sentence.matches(SEND_A_MAIL)) {
					Desktop.getDesktop().mail();
					return "Ok.";
				}

			} catch (final IOException e) {
				return handleErrorMessage(lang);
			}
		}

		return handleErrorMessage(lang);
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
			set.add(ENVOI_UN_MAIL);
		} else {
			set.add(SEND_A_MAIL);
		}

		return set;
	}

	private String handleErrorMessage(final Language lang) {
		if (lang == Language.french) {
			return "Impossible d'ouvrir votre client mail.";
		}
		return "Cannot open your mail client.";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleRecoveryService(java.lang.String, com.asilane.core.Language)
	 */
	@Override
	public String handleRecoveryService(final String sentence, final Language lang) {
		return null;
	}
}