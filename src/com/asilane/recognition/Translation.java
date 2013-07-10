package com.asilane.recognition;

import java.util.HashMap;
import java.util.Map;

/**
 * @author walane
 * 
 *         This class implements Singleton pattern for better performances
 */
public class Translation {
	private static Translation INSTANCE;
	private Map<String, String> frenchMap;

	private Translation() {
		initMap();
	}

	public static Translation getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Translation();
		}
		return INSTANCE;
	}

	private void initMap() {
		frenchMap = new HashMap<String, String>();
		frenchMap.put("bonjour", "hello");
	}

	public String getTranslation(final String instruction, final Language language) {
		switch (language) {
		case french:
			return frenchMap.get(instruction);
		default:
			return null;
		}
	}
}