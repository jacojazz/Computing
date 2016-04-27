package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class TrailParticle {

	int x, y, lifetime, age, r, g, b;
	double xv, yv, velocity;
	Color color;

	public TrailParticle(int x, int y) {
		this.x = x;
		this.y = y;

		lifetime = 4;
		age = 0;

		velocity = 1.5 + randDouble(0.0, 10);

		xv = randInt(-1, 1);
		yv = randInt(1, 3);

		Random rand = new Random();
		
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
		age++;
	}

	void paint(Graphics2D g2d) {
		g2d.setColor(color);
		g2d.fillOval(x, y, 3, 3);
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
