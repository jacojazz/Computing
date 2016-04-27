package gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Target {

	int x, y, width, height, health, xv, yv, xmv;
	float alpha;
	Rectangle rect;
	Rectangle healthBar;
	int frameLastFired = 0;

	public Target(int x, int y, int xv, int size, float alpha) {
		this.x = x;
		this.y = y;
		this.width = size;
		this.height = size;
		this.xv = xv;
		this.alpha = alpha;

		this.health = size;

		rect = new Rectangle(x, y, width, height);
		healthBar = new Rectangle(x, y - 7, health, 2);
	}

	private AlphaComposite makeComposite(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type, alpha));
	}

	void update() {
		y += yv;
		if (x < 1) {
			xv *= -1;
		} else if (x > Game.width - 30) {
			xv *= -1;
		}
		x += xv;

		rect.setBounds(x, y, width, height);
		healthBar.setBounds(x, y - 7, health, 2);
		if (x > (Game.ship.getX() + 10) && x < (Game.ship.getX() + Game.ship.getSize()) && Game.frames > (frameLastFired + 25) && Game.ship.getY() > y) {
			frameLastFired = Game.frames;
			Game.enemyBulletList.add(new EnemyBullet(x, y + (height / 2), 0, 3, alpha));
		} else if (x > (Game.ship.getX() + 10) && x < (Game.ship.getX() + Game.ship.getSize()) && Game.frames > (frameLastFired + 25) && Game.ship.getY() < y) {
			frameLastFired = Game.frames;
			Game.enemyBulletList.add(new EnemyBullet(x, y + height, 0, -3, alpha));
		}
	}

	void paint(Graphics2D g2d) {
		Composite originalComposite = g2d.getComposite();
		g2d.setComposite(makeComposite(alpha));
		g2d.setColor(Color.BLACK);
		g2d.fill(rect);
		g2d.setColor(Color.RED);
		g2d.fill(healthBar);
		g2d.setColor(Color.BLACK);
		g2d.setComposite(originalComposite);
	}
}
