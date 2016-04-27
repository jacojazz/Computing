package gui;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Explosion {

	int x, y, pNumber, r, g, b, repeats;
	Clip clip;

	public Explosion(int x, int y, int pNumber, String calledClassName, boolean enableSound) {
		this.x = x + 10;
		this.y = y + 10;
		this.pNumber = pNumber;

		initExplosion(calledClassName);
		if (enableSound) {
			try {
				AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("music/explosion.wav"));
				clip = AudioSystem.getClip();
				clip.open(ais);
				clip.start();
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void initExplosion(String className) {
		if (className.equals("gui.Game")) {
			while (pNumber > 0) {
				Game.particleList.add(new Particle(30, x, y));
				pNumber--;
			}
		} else if (className.equals("gui.Menu")) {
			while (pNumber > 0) {
				Menu.particleList.add(new Particle(30, x, y));
				pNumber--;
			}
		}
	}
}
