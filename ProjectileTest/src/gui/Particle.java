/*
 * TYPES
 * 0 = manual
 * 1 = snow
 * 2 = rain
 * 3 = rain splash
 *  
 */
package gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.Random;

public class Particle {
	double size, x, y, xVelocity, yVelocity, lifetime, age = 0;
	Ellipse2D rect;
	Color color;
	boolean touchFloor = false;
	float alpha;

	double yGRAVITY = Game.yGRAVITY;
	double xGRAVITY = Game.xGravity;

	double FRICTION_COEFFICIENT;
	double BOUNCE_COEFFICIENT;

	int TYPE;

	Particle(double x, double y, double xVelocity, double yVelocity, double lifetime, double size, float alpha, int type) {
		this.x = x;
		this.y = y;
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
		this.size = size;
		this.alpha = alpha;
		this.TYPE = type;

		if (TYPE == 0) {
			color = Color.WHITE;
			this.FRICTION_COEFFICIENT = 0.7;
			this.BOUNCE_COEFFICIENT = 0.7;
			this.lifetime = lifetime * Game.TARGET_FPS;
			rect = new Ellipse2D.Double(x, y, size, size);
		} else if (TYPE == 1) {
			color = new Color(0xCFF1FC);
			this.lifetime = lifetime * Game.TARGET_FPS;
			this.FRICTION_COEFFICIENT = Game.randDouble(0.5, 0.8);
			this.BOUNCE_COEFFICIENT = Game.randDouble(0, 0.1);
			rect = new Ellipse2D.Double(x, y, size, size);
		} else if (TYPE == 2) {
			color = new Color(0x5FA3BA);
			this.lifetime = lifetime * Game.TARGET_FPS;
			rect = new Ellipse2D.Double(x, y, size, size * 3);
		} else if (TYPE == 3) {
			color = new Color(0x5FA3BA);
			this.lifetime = lifetime * Game.TARGET_FPS;
			rect = new Ellipse2D.Double(x, y, size, size);
		}
	}

	private AlphaComposite makeComposite(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type, alpha));
	}

	public void update() {
		yGRAVITY = Game.yGRAVITY;
		xGRAVITY = Game.xGravity;
		age++;

		// setY(Game.height - rect.getHeight());
		// yVelocity = -(yVelocity * BOUNCE_VALUE);
		if (TYPE == 1 || TYPE == 3) {
			if (isTouchingFloor() == false) {
				yVelocity += yGRAVITY;
			} else if (isTouchingFloor() == true && yVelocity > 0) {
				yVelocity = bounce();
				xVelocity *= FRICTION_COEFFICIENT;
			} else {
				setY(Game.height - rect.getHeight());
				yVelocity = 0;
			}
		} else if (TYPE == 2) {
			yVelocity += yGRAVITY;
		} else if (TYPE == 0) {
			if (Game.gravityMode == 0 || Game.gravityMode == 1) {
				if (isTouchingFloor() == false) {
					yVelocity += yGRAVITY;
				} else if (isTouchingFloor() == true && yVelocity > 0) {
					yVelocity = bounce();
					xVelocity *= FRICTION_COEFFICIENT;
				} else {
					setY(Game.height - rect.getHeight());
					yVelocity = 0;
				}
			} else if (Game.gravityMode == 2) {
				System.out.println("Steve");
				if (rect.getY() > (Game.height / 2)) {
					yVelocity -= yGRAVITY;
				} else {
					yVelocity += yGRAVITY;
				}

				if (rect.getX() > (Game.width / 2)) {
					xVelocity -= xGRAVITY;
				} else {
					xVelocity += xGRAVITY;
				}
			}
		}

		if (xVelocity != 0 && isTouchingFloor() == true) {
			xVelocity *= FRICTION_COEFFICIENT;
		}

		x += xVelocity;
		y += yVelocity;

		rect = new Ellipse2D.Double(x, y, rect.getWidth(), rect.getHeight());
	}

	public boolean isTouchingFloor() {
		if (getY() >= (Game.height - rect.getHeight())) {
			return true;
		} else {
			return false;
		}
	}

	public double bounce() {
		if (yVelocity < 0.2 && yVelocity > -0.2) {
			setY(Game.height - rect.getHeight());
			return 0;
		} else {
			setY(Game.height - rect.getHeight());
			return -(yVelocity * BOUNCE_COEFFICIENT);
		}
	}

	public void paint(Graphics2D g2d) {
		Composite originalComposite = g2d.getComposite();
		g2d.setComposite(makeComposite(alpha));

		AffineTransform transform = new AffineTransform();
		transform.rotate(-getAngle(xVelocity, yVelocity), rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);
		AffineTransform old = g2d.getTransform();

		g2d.setTransform(transform);

		g2d.setColor(color);
		g2d.fill(rect);

		g2d.setTransform(old);

		g2d.setComposite(originalComposite);
	}

	public static double getAngle(double x, double y) {
		return 1.5 * Math.PI - Math.atan2(y, x);
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getxVelocity() {
		return xVelocity;
	}

	public void setxVelocity(double xVelocity) {
		this.xVelocity = xVelocity;
	}

	public double getyVelocity() {
		return yVelocity;
	}

	public void setyVelocity(double yVelocity) {
		this.yVelocity = yVelocity;
	}

	public double getAge() {
		return age;
	}

	public void setAge(double age) {
		this.age = age;
	}

	public double getLifetime() {
		return lifetime;
	}

	public void setLifetime(double lifetime) {
		this.lifetime = lifetime;
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
