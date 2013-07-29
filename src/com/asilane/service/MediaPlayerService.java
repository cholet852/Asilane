package com.asilane.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javazoom.jl.player.Player;

import com.asilane.core.Language;
import com.asilane.core.facade.history.HistoryNode;
import com.asilane.core.facade.history.HistoryTree;
import com.darkprograms.speech.synthesiser.PlayerThread;

/**
 * @author walane
 * 
 */
public class MediaPlayerService implements IService {
	private PlayerThread playerThread;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.Service#handleService(java.lang.String, com.asilane.recognition.Language)
	 */
	@Override
	public String handleService(final String sentence, final Language lang, final HistoryTree historyTree) {
		if (sentence.equals("stop")) {
			HistoryNode tmpNode = historyTree.getFirstNode();
			while (!tmpNode.isLeaf()) {
				tmpNode = (tmpNode.getLeftSon() == null) ? tmpNode.getRightSon() : tmpNode.getRightSon();
				if (tmpNode.getService().getClass().equals(MediaPlayerService.class)) {
					((MediaPlayerService) tmpNode.getService()).stopPlayerThread();
				}
			}
		} else {
			try {
				final List<String> filesList = listFiles("/home/walane/Musique/");
				final Player player = new Player(new FileInputStream(filesList.get(new Random().nextInt(filesList
						.size()))));
				playerThread = new PlayerThread(player);
				playerThread.start();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}

		return "C'est parti !";
	}

	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void stopPlayerThread() {
		playerThread.stop();
	}

	/**
	 * List all files in a folder
	 * 
	 * @param directory
	 * @return all files in a folder
	 */
	private List<String> listFiles(final String directory) {
		final List<String> files = new ArrayList<String>();

		for (final File file : new File(directory).listFiles()) {
			if (file.isDirectory()) {
				files.addAll(listFiles(file.getAbsolutePath()));
			} else {
				files.add(file.getAbsolutePath());
			}
		}

		return files;
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
			set.add("stop");
		} else {
		}

		return set;
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