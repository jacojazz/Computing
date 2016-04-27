package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Mine {
	private int x, y;
	private Ellipse2D circle;
	boolean ALIVE = true;
	Color color = Color.GRAY;
	Clip clip;
	
	Mine(int xInitial, int yInitial) {
		x = xInitial;
		y = yInitial;
		
		circle = new Ellipse2D.Double(x, y, 10, 10);
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("music/minebeep.wav"));
					clip = AudioSystem.getClip();
					clip.open(ais);
					clip.start();
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
					e1.printStackTrace();
				}
				color = Color.RED;
			}
		}, 4000);
		timer.schedule(new TimerTask() {
			public void run() {
				new Explosion(x, y, 50, "gui.Game", true);
				ALIVE = false;
			}	
		}, 5000);
	}
	
	void update() {
		
	}
	
	void paint(Graphics2D g2d) {
		g2d.setColor(color);
		g2d.fill(circle);
		g2d.setColor(Color.BLACK);
	}
}