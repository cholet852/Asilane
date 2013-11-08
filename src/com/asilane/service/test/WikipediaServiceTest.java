/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.service.test;

import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.asilane.core.facade.ServiceDispatcher;

public class WikipediaServiceTest {
	private ServiceDispatcher dispatcher;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testFrench() {
		dispatcher = ServiceDispatcher.getInstance(Locale.FRANCE);
		assertNotNull(dispatcher.getService("c'est quoi un ordinateur"));
		assertNotNull(dispatcher.getService("qu'est-ce qu'un ordinateur"));
		assertNotNull(dispatcher.getService("qu'est-ce que la vie"));
	}

	@Test
	public void testEnglish() {
		dispatcher = ServiceDispatcher.getInstance(Locale.ENGLISH);
		assertNotNull(dispatcher.getService("what is a computer"));
	}
}
