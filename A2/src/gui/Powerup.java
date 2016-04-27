package gui;

import java.awt.Graphics2D;

public class Powerup {
	private String type;
	private int x, speed;
	
	public Powerup(String type, int x, int speed) {
		this.type = type;
		this.x = x;
		this.speed = speed;
	}
	
	public void update() {
		x += speed;
	}
	
	public void paint(Graphics2D g2d) {
		
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
}
