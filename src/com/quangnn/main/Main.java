package com.quangnn.main;

import com.quangnn.sound.AudioManager;
import com.quangnn.ui.GUI;

public class Main {
	public static void main(String[] args) {
		GUI gui = new GUI();
		gui.setVisible(true);

		AudioManager audioManager = new AudioManager();
		audioManager.getmAudioPlayGame().play();
	}
}
