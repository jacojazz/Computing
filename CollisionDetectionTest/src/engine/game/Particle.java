package engine.game;

import java.util.Timer;
import java.util.TimerTask;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.line.Line2D;
import engine.utils.Constants;

public class Particle extends Circle2D {
	private Vector2D velocity;
	private Vector2D force;
	private double mass;
	private boolean colliding;
	private boolean active = true;
	private Point2D oldPosition;

	Particle(Point2D center, double radius, double mass) {
		super(center, radius);
		this.mass = mass;
		this.force = new Vector2D(0, Game.GRAVITY);
	}

	Particle(Point2D center, double radius, double mass, Vector2D velocity) {
		super(center, radius);
		this.mass = mass;
		this.force = new Vector2D(0, Game.GRAVITY);
		this.velocity = velocity;
	}

	boolean checkCollision(Particle p, Particle p2) {
		try {
			double xDif = p.center().getX() - p2.center().getX();
			double yDif = p.center().getY() - p2.center().getY();
			double distanceSquared = (xDif * xDif) + (yDif * yDif);
			boolean collision = distanceSquared <= (p.radius() + p2.radius()) * (p.radius() + p2.radius());
			return collision;
		} catch (Exception e) {
			return false;
		}
	}

	boolean inLineCollisionRange(Line2D l) {
		try {
			if (l.distance(center()) <= radius()) {
				double penetrationDepth = radius() - l.distance(center());
				Vector2D resolution = l.perpendicular(center()).direction().normalize().times(penetrationDepth);
				setPosition(center().plus(resolution));
				return true;
			} else {
				return false;
			}
		} catch (NullPointerException e) {
			return false;
		}
	}

	boolean inParticleCollisionRange(Particle p2) {
		if (isActive() || p2.isActive()) {
			try {
				if (distance(p2.center()) < (radius() + p2.radius()) * 2) {
					return true;
				} else {
					return false;
				}
			} catch (NullPointerException e) {
				return true;
			}
		} else {
			return false;
		}
	}

	Vector2D reflect(Line2D l) {
		Vector2D n = l.perpendicular(center()).direction().normalize();
		Vector2D v = velocity.minus(n.times(2 * (n.dot(velocity))));
		return new Vector2D(v.getX() * Constants.restitution, v.getY() * Constants.restitution);
	}

	public void resolveCollision(Particle p, Particle p2) {
		p.setActive(true);
		p2.setActive(true);
		colliding = true;
		Vector2D delta = new Vector2D(p.center().minus(p2.center()));
		double d = delta.norm();
		Vector2D mtd = delta.times(((p.radius() + p2.radius()) - d) / d);
		double im1 = (1 / p.getMass());
		double im2 = (1 / p2.getMass());
		p.setPosition(p.center().plus(mtd.times(im1 / (im1 + im2))));
		p2.setPosition(p2.center().minus(mtd.times(im2 / (im1 + im2))));
		Vector2D v = (p.getVelocity().minus(p2.getVelocity()));
		double vn = v.dot(mtd.normalize());
		if (vn > 0.0f)
			return;
		double i = ((-(1.0f + Constants.restitution) * vn) / (im1 + im2));
		// double p1PD = p.distance(p2.point(p2.project(p2.center())));
		// double p2PD = p2.distance(p.point(p.project(p.center())));
		Vector2D impulse = mtd.normalize().times(i);
		// p.setVelocity(p.getVelocity().plus(impulse.times(im1).times(tolerance(p2PD,
		// p2.radius()))));
		// p2.setVelocity(p2.getVelocity().minus(impulse.times(im2).times(tolerance(p1PD,
		// p.radius()))));
		p.setVelocity(p.getVelocity().plus(impulse.times(im1)));
		p2.setVelocity(p2.getVelocity().minus(impulse.times(im2)));
	}

	double tolerance(double penetrationDepth, double radius) {
		double result = penetrationDepth / radius;
		if (result > 1) {
			return 1;
		} else {
			return result;
		}

	}

	void checkActive() {
		if (Game.frames % Game.TARGET_FPS == 0) {
			oldPosition = center();
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run() {
					if (oldPosition.almostEquals(center(), 1)) {
						setActive(false);
					}
				}
			}, 100);
		}
	}

	void update() {
		for (int particle2Iterator = 0; particle2Iterator < Game.pList.size(); particle2Iterator++) {
			Particle p2 = Game.pList.get(particle2Iterator);
			if (inParticleCollisionRange(p2)) {
				if (checkCollision(this, p2) && !equals(p2)) {
					resolveCollision(this, p2);
				} else {
					colliding = false;
				}
			}
		}

		for (int lineIterator = 0; lineIterator < Game.lList.size(); lineIterator++) {
			Line2D l = Game.lList.get(lineIterator);
			if (inLineCollisionRange(l)) {
				velocity = reflect(l);
			}
		}

		velocity = velocity.plus(force);
		Point2D p = center().plus(velocity);
		setPosition(p.getX(), p.getY());
		checkActive();
	}

	public void setPosition(double x, double y) {
		xc = x;
		yc = y;
		theta = velocity.angle();
	}

	public void setPosition(Object obj) {
		if (obj instanceof Vector2D) {
			Vector2D v = (Vector2D) obj;
			xc = v.getX();
			yc = v.getY();
		} else if (obj instanceof Point2D) {
			Point2D p = (Point2D) obj;
			xc = p.getX();
			yc = p.getY();
		} else {
			return;
		}
	}

	public Vector2D getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public boolean isColliding() {
		return colliding;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
