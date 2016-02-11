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
import java.util.Random;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.conic.Circle2D;

public class Particle extends Circle2D {
	double lifetime, age = 0;
	Color color;
	boolean touchFloor = false;
	float alpha;

	Vector2D velocity;

	double yGRAVITY = Game.yGRAVITY;
	double xGRAVITY = Game.xGRAVITY;

	double FRICTION_COEFFICIENT;
	double BOUNCE_COEFFICIENT;

	int TYPE;

	Particle(Point2D center, double radius, Vector2D velocity, double lifetime, float alpha, int type) {
		super(center, radius / 2);
		this.alpha = alpha;
		this.TYPE = type;
		this.velocity = velocity;
		this.lifetime = lifetime * Game.TARGET_FPS;

		if (TYPE == 0) {
			color = Color.WHITE;
			this.FRICTION_COEFFICIENT = 0.7;
			this.BOUNCE_COEFFICIENT = 0.7;

		} else if (TYPE == 1) {
			color = new Color(0xCFF1FC);
			this.FRICTION_COEFFICIENT = Game.randDouble(0.5, 0.8);
			this.BOUNCE_COEFFICIENT = Game.randDouble(0, 0.1);
		} else if (TYPE == 2 && TYPE == 3) {
			color = new Color(0x5FA3BA);
		}
	}

	private AlphaComposite makeComposite(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type, alpha));
	}

	public void update() {
		yGRAVITY = Game.yGRAVITY;
		xGRAVITY = Game.xGRAVITY;
		age++;

		if (TYPE == 1 || TYPE == 3) {
			if (Game.gravityMode != 2) {
				if (isTouchingFloor() == false) {
					velocity = new Vector2D(velocity.getX(), velocity.getY() + yGRAVITY);
				} else if (isTouchingFloor() == true && velocity.getY() > 0) {
					velocity = new Vector2D(velocity.getX(), bounce());
					velocity = new Vector2D(velocity.getX() * FRICTION_COEFFICIENT, velocity.getY());
				} else {
					yc = Game.height - radius();
					velocity = new Vector2D(velocity.getX(), 0);
				}
			} else {
				if (center().getY() > (Game.height / 2)) {
					velocity = new Vector2D(velocity.getX(), velocity.getY() - yGRAVITY);
				} else {
					velocity = new Vector2D(velocity.getX(), velocity.getY() + yGRAVITY);
				}

				if (center().getX() > (Game.width / 2)) {
					velocity = new Vector2D(velocity.getX() - xGRAVITY, velocity.getY());
				} else {
					velocity = new Vector2D(velocity.getX() + xGRAVITY, velocity.getY());
				}
			}
		} else if (TYPE == 2) {
			velocity = new Vector2D(velocity.getX(), velocity.getY() + yGRAVITY);
		} else if (TYPE == 0) {
			if (Game.gravityMode == 0 || Game.gravityMode == 1) {
				if (isTouchingFloor() == false) {
					velocity = new Vector2D(velocity.getX(), velocity.getY() + yGRAVITY);
				} else if (isTouchingFloor() == true && velocity.getY() > 0) {
					velocity = new Vector2D(velocity.getX(), bounce());
					velocity = new Vector2D(velocity.getX() * FRICTION_COEFFICIENT, velocity.getY());
				} else {
					velocity = new Vector2D(velocity.getX(), Game.height - radius());
					velocity = new Vector2D(velocity.getX(), 0);
				}
			} else if (Game.gravityMode == 2) {
				if (center().getY() > (Game.height / 2)) {
					velocity = new Vector2D(velocity.getX(), velocity.getY() - yGRAVITY);
				} else {
					velocity = new Vector2D(velocity.getX(), velocity.getY() + yGRAVITY);
				}

				if (center().getX() > (Game.width / 2)) {
					velocity = new Vector2D(velocity.getX() - xGRAVITY, velocity.getY());
				} else {
					velocity = new Vector2D(velocity.getX() + xGRAVITY, velocity.getY());
				}
			}
		}

		if (velocity.getX() != 0 && isTouchingFloor() == true) {
			velocity = new Vector2D(velocity.getX() * FRICTION_COEFFICIENT, velocity.getY());
		}

		xc += velocity.getX();
		yc += velocity.getY();
	}

	public boolean isTouchingFloor() {
		if (center().getY() >= (Game.height - radius())) {
			return true;
		} else {
			return false;
		}
	}

	public double bounce() {
		if (velocity.getY() < 0.2 && velocity.getY() > -0.2) {
			yc = Game.height - radius();
			return 0;
		} else {
			yc = Game.height - radius();
			return -(velocity.getY() * BOUNCE_COEFFICIENT);
		}
	}

	public void paint(Graphics2D g2d) {
		Composite originalComposite = g2d.getComposite();
		g2d.setComposite(makeComposite(alpha));

		AffineTransform transform = new AffineTransform();
		transform.rotate(-getAngle(velocity), center().getX() + radius(), center().getY() + radius());
		AffineTransform old = g2d.getTransform();

		g2d.setTransform(transform);
		g2d.setColor(color);
		g2d.fill(this.asAwtShape());
		g2d.setTransform(old);

		g2d.setComposite(originalComposite);
	}

	public static double getAngle(Vector2D velocity) {
		return 1.5 * Math.PI - Math.atan2(velocity.getY(), velocity.getX());
	}

	public double getAge() {
		return age;
	}

	public double getLifetime() {
		return lifetime;
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
