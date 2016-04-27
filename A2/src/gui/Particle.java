package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class Particle {
	int x, y, r, g, b;
	int lifetime, age, size;
	private double xv, yv;
	private Color color;

	public Particle(int lifetime, int x, int y) {
		age = 0;
		this.lifetime = lifetime;
		this.x = x;
		this.y = y;
		size = randInt(1, 15); //15

		int angle = randInt(0, 359);
		double velocity = 1.5 + randDouble(0.0, 3);
		Random rand = new Random();

		xv = Math.sin(angle) * velocity;
		yv = Math.cos(angle) * velocity;

		if (rand.nextInt(10) < 4) {
			int blackRandom = rand.nextInt(101);
			r = blackRandom;
			g = blackRandom;
			b = blackRandom;
		} else {
			r = 255;
			g = rand.nextInt(256);
			b = 0;
		}

		color = new Color(r, g, b);
	}

	void update() {
		x += xv;
		y += yv;
		//xv *= 0.9;
		//yv *= 0.9;
		age++;
	}

	void paint(Graphics2D g2d) {
		g2d.setColor(color);
		g2d.fillOval(x, y, size, size);
		g2d.setColor(Color.BLACK);
	}

	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	public static double randDouble(double min, double max) {
		Random r = new Random();
		double result = (r.nextInt(101) - 10) / 10.0;
		return result;
	}
}
