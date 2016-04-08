package com.quangnn.sound;

import javax.sound.sampled.Clip;

public class AudioManager {
	private AudioEffect mAudioPlayGame;

	public AudioManager() {
		mAudioPlayGame = new AudioEffect("TetrisGame.wav");
		mAudioPlayGame.loop(Clip.LOOP_CONTINUOUSLY);

	}

	public AudioEffect getmAudioPlayGame() {
		return mAudioPlayGame;
	}

	public void setmAudioPlayGame(AudioEffect mAudioPlayGame) {
		this.mAudioPlayGame = mAudioPlayGame;
	}
}
