package com.darkprograms.speech.synthesiser;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 * This is for play MP3 in another thread
 * 
 * @author walane
 */
public class PlayerThread extends Thread {
	private final Player p;

	/**
	 * Create a new PlayerThread for play MP3 in another thread
	 * 
	 * @param p
	 */
	public PlayerThread(final Player p) {
		this.p = p;
	}

	@Override
	public void run() {
		try {
			p.play();
		} catch (final JavaLayerException e) {
			// Nothing catched, oh god !
		}
	}
}