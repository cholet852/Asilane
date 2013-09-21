package com.asilane.service.test;

import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.asilane.core.facade.ServiceDispatcher;

public class DateServiceTest {
	private ServiceDispatcher dispatcher;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testFrench() {
		dispatcher = ServiceDispatcher.getInstance(Locale.FRANCE);
		assertNotNull(dispatcher.getService("quel jour sommes nous"));
		assertNotNull(dispatcher.getService("on est quel jour"));
		assertNotNull(dispatcher.getService("quel jour on est"));
		assertNotNull(dispatcher.getService("quel est la date"));
	}

	@Test
	public void testEnglish() {
		dispatcher = ServiceDispatcher.getInstance(Locale.ENGLISH);
		assertNotNull(dispatcher.getService("what time is it"));
	}
}
