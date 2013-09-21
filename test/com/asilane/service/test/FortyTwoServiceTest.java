package com.asilane.service.test;

import static org.junit.Assert.assertSame;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.asilane.core.facade.Facade;

public class FortyTwoServiceTest {
	private Facade facade;

	@Before
	public void setUp() throws Exception {
		facade = new Facade();
	}

	@Test
	public void testFrench() {
		final Locale lang = Locale.FRANCE;
		assertSame("42", facade.handleSentence("quel est le sens de la vie", lang));
		assertSame("42", facade.handleSentence("quelle est la réponse à l'univers", lang));
		assertSame("42", facade.handleSentence("quel est le nombre ultime", lang));
	}

	@Test
	public void testEnglish() {
		final Locale lang = Locale.ENGLISH;
		assertSame("42", facade.handleSentence("what is the answer of the universe", lang));
		assertSame("42", facade.handleSentence("what is the ultimate number", lang));
	}
}