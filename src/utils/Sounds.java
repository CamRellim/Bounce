package utils;

import java.io.*;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sounds {
	
	
	public void play(File sound) throws Exception {
		new Thread(new Runnable() {
			
			public void run() {
				Clip clip = null;
				try {
					clip = AudioSystem.getClip();
					clip.open(AudioSystem.getAudioInputStream(sound));
					clip.start();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(clip.getMicrosecondLength()/1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}