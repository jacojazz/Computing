package gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class EnemyBullet {
	int x, y, xVelocity, yVelocity;
	float alpha;
	Rectangle rect;
	
	EnemyBullet(int xInitial, int yInitial, int xVelocity, int yVelocity, float alpha) {
		x = xInitial + 8;
		y = yInitial;
		
		this.alpha = alpha;
		
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
		
		rect = new Rectangle(x, y, 4, 10);
	}
	
	void update() {
		x += xVelocity;
		y += yVelocity;
		rect.setBounds(x, y, 4, 10);
	}
	
	private AlphaComposite makeComposite(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type, alpha));
	}
	
	void paint(Graphics2D g2d) {
		Composite originalComposite = g2d.getComposite();
		g2d.setComposite(makeComposite(alpha));
		g2d.setColor(Color.DARK_GRAY);
		g2d.fill(rect);
		g2d.setColor(Color.BLACK);
		g2d.setComposite(originalComposite);
	}
	
	int getY() {
		return y;
	}
}
