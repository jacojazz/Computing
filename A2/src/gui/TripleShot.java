package gui;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class TripleShot {
	private int x, y;
	Clip clip;
	
	TripleShot(int xInitial, int yInitial) {
		this.x = xInitial + 8;
		this.y = yInitial;
		Game.bulletList.add(new Bullet(x, y, -1, -4, false));
		Game.bulletList.add(new Bullet(x, y, 0, -4, false));
		Game.bulletList.add(new Bullet(x, y, 1, -4, false));
		
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("music/tripleshot.wav"));
			clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
			e1.printStackTrace();
		}
	}

	int getY() {
		return y;
	}
}
