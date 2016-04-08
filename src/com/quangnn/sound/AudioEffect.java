package com.quangnn.sound;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioEffect implements LineListener {

	private Clip clip;
	private boolean isDone;

	public AudioEffect(String name) {

		URL url = getClass().getResource("/sound/" + name);
		try {
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip();
			clip.open(inputStream);
			clip.addLineListener(this);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void play() {

		new Thread() {
			@Override
			public void run() {
				isDone = false;
				clip.start();
				while (!isDone) {
					try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();

	}

	public void stop() {
		if (clip.isRunning()) {
			clip.stop();
			clip.close();
		}
	}

	public void loop(int count) {
		clip.loop(count);
	}

	@Override
	public void update(LineEvent event) {
		if (event.getType() == LineEvent.Type.CLOSE
				|| event.getType() == LineEvent.Type.STOP) {
			isDone = true;
		}
	}

}
