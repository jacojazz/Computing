package engine.game;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.line.Line2D;

public class Particle extends Circle2D {
	private Vector2D velocity;
	private Vector2D force;
	private double mass;
	private boolean colliding;

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
		double xDif = p.center().getX() - p2.center().getX();
		double yDif = p.center().getY() - p2.center().getY();
		double distanceSquared = (xDif * xDif) + (yDif * yDif);
		boolean collision = distanceSquared <= (p.radius() + p2.radius()) * (p.radius() + p2.radius());
		return collision;
	}

	boolean inLineCollisionRange(Line2D l) {
		if (l.distance(center()) <= radius()) {
			return true;
		} else {
			return false;
		}
	}

	boolean inParticleCollisionRange(Particle p2) {
		if (distance(p2.center()) < (radius() + p2.radius()) * 2) {
			return true;
		} else {
			return false;
		}
	}

	Vector2D reflect(Line2D l) {
		setPosition(center());
		Vector2D n2 = l.perpendicular(center()).direction().normalize();
		Vector2D v = velocity.minus(n2.times(2 * (n2.dot(velocity))));
		return new Vector2D(v.getX() * Constants.restitution, v.getY() * Constants.restitution);
	}

	public void resolveCollision(Particle p, Particle p2) {
		colliding = true;
		Vector2D delta = new Vector2D(p.center().minus(p2.center()));
		float d = (float) delta.norm();
		Vector2D mtd = delta.times(((p.radius() + p2.radius()) - d) / d);
		float im1 = (float) (1 / p.getMass());
		float im2 = (float) (1 / p2.getMass());
		p.setPosition(p.center().plus(mtd.times(im1 / (im1 + im2))));
		p2.setPosition(p2.center().minus(mtd.times(im2 / (im1 + im2))));
		Vector2D v = (p.getVelocity().minus(p2.getVelocity()));
		float vn = (float) v.dot(mtd.normalize());
		if (vn > 0.0f)
			return;
		float i = (float) ((-(1.0f + Constants.restitution) * vn) / (im1 + im2));
		Vector2D impulse = mtd.normalize().times(i);
		p.setVelocity(p.getVelocity().plus(impulse.times(im1)));
		p2.setVelocity(p2.getVelocity().minus(impulse.times(im2)));
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
	}

	public void setPosition(double x, double y) {
		xc = x;
		yc = y;
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
}
