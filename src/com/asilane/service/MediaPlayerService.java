package com.asilane.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import com.asilane.core.Language;
import com.asilane.core.PlayerThread;

/**
 * @author walane
 * 
 */
public class MediaPlayerService implements IService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Language)
	 */
	@Override
	public String handleService(final String sentence, final Language lang) {
		try {
			final Player player = new Player(new FileInputStream(
					"/home/walane/Musique/Red Hot Chili Peppers/Unknown Album/Snow [Hey Oh].mp3"));
			new PlayerThread(player).start();
		} catch (FileNotFoundException | JavaLayerException e) {
			e.printStackTrace();
		}

		return "C'est parti !";
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
			set.add("joue de la musique");
		} else {
		}

		return set;
	}

	@Override
	public String handleRecoveryService(final String sentence, final Language lang) {
		// TODO Auto-generated method stub
		return null;
	}
}