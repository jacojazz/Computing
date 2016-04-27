package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Bullet {
	int x, y, xVelocity, yVelocity;
	Rectangle rect;
	Clip clip;

	Bullet(int xInitial, int yInitial, int xVelocity, int yVelocity, boolean enableSound) {
		x = xInitial + 8;
		y = yInitial;

		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;

		rect = new Rectangle(x, y, 4, 10);
		if (enableSound) {
			try {
				AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("music/bullet.wav"));
				clip = AudioSystem.getClip();
				clip.open(ais);
				clip.start();
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
				e1.printStackTrace();
			}
		}
	}

	void update() {
		x += xVelocity;
		y += yVelocity;
		rect.setBounds(x, y, 4, 10);
	}

	void paint(Graphics2D g2d) {
		g2d.setColor(Color.DARK_GRAY);
		g2d.fill(rect);
		g2d.setColor(Color.BLACK);
	}

	int getY() {
		return y;
	}
}
