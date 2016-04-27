package gui;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Rocket {
	
	int x, y, velocity, trailDensity;
	Rectangle rocket;
	Clip clip;
	
	public Rocket(int x, int y, int velocity) {
		this.x = x;
		this.y = y + 15;
		this.velocity = velocity;
		
		trailDensity = 5;
		rocket = new Rectangle(x, y, 5, 15);
		
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("music/rocket.wav"));
			clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
			e1.printStackTrace();
		}
	}
	
	void update() {
		y -= velocity;
		rocket.setBounds(x, y, 5, 15);
		for(int i = 0; i < trailDensity; i++) {
			Game.trailPList.add(new TrailParticle(x + 2, y + 15));
		}
	}
	
	void paint(Graphics2D g2d) {
		g2d.fill(rocket);
	}
}
