package com.asilane.service.test;

import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.asilane.core.facade.ServiceDispatcher;

public class IPServiceTest {
	private ServiceDispatcher dispatcher;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testFrench() {
		dispatcher = ServiceDispatcher.getInstance(Locale.FRANCE);
		assertNotNull(dispatcher.getService("quel est mon ip"));
		assertNotNull(dispatcher.getService("quel est mon ip locale"));
	}

	@Test
	public void testEnglish() {
		dispatcher = ServiceDispatcher.getInstance(Locale.ENGLISH);
		assertNotNull(dispatcher.getService("what is my ip"));
	}
}
