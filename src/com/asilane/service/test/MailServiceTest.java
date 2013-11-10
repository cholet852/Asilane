/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.service.test;

import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.asilane.core.facade.Facade;
import com.asilane.core.facade.NoServiceFoundException;
import com.asilane.core.facade.Question;

public class MailServiceTest {
	private Facade facade;

	@Before
	public void setUp() throws Exception {
		facade = new Facade(null);
	}

	@Test
	public void testFrench() throws NoServiceFoundException {
		final Locale lang = Locale.FRANCE;
		assertTrue(facade.handleSentence(new Question("envoi un mail à foo@bar.com", lang)).getSpeechedResponse().contains("foo@bar.com"));
		assertTrue(facade.handleSentence(new Question("envoi un mail à foo@bar.com Bonjour comment allez-vous", lang))
				.getSpeechedResponse().contains("foo@bar.com"));
		assertTrue(facade.handleSentence(new Question("envoi un mail à foo arobase bar point com Bonjour comment allez-vous", lang))
				.getSpeechedResponse().contains("foo@bar.com"));
	}

	@Test
	public void testEnglish() throws NoServiceFoundException {
		final Locale lang = Locale.ENGLISH;
		assertTrue(facade.handleSentence(new Question("send a mail to foo@bar.com", lang)).getSpeechedResponse().contains("foo@bar.com"));
		assertTrue(facade.handleSentence(new Question("send a mail to foo@bar.com Hello how are you ?", lang)).getSpeechedResponse()
				.contains("foo@bar.com"));
		assertTrue(facade.handleSentence(new Question("send a mail to foo arobas bar dot com Bonjour comment allez-vous", lang))
				.getSpeechedResponse().contains("foo@bar.com"));
	}
}