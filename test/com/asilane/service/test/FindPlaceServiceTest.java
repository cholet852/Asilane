package com.asilane.service.test;

import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.asilane.core.facade.ServiceDispatcher;

public class FindPlaceServiceTest {
	private ServiceDispatcher dispatcher;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testFrench() {
		dispatcher = ServiceDispatcher.getInstance(Locale.FRANCE);
		assertNotNull(dispatcher.getService("où se trouve Lille"));
		assertNotNull(dispatcher.getService("où se situe Lille"));

		assertNotNull(dispatcher.getService("itinéraire de Lille à Paris"));
		assertNotNull(dispatcher.getService("itinéraire entre Lille et Paris"));
	}

	@Test
	public void testEnglish() {
		dispatcher = ServiceDispatcher.getInstance(Locale.ENGLISH);
		assertNotNull(dispatcher.getService("where is Lille"));
	}
}