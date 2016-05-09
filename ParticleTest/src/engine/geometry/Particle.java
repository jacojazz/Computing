package engine.geometry;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.conic.Circle2D;
import engine.dynamics.Mass;
import engine.utils.Constants;

public class Particle extends Circle2D {
	Mass mass;
	Vector2D velocity, force;

	public Particle(Point2D center, double radius) {
		this(center, radius, new Vector2D(0, 0));
	}

	public Particle(Point2D center, double radius, Vector2D velocity) {
		super(center, radius);
		this.velocity = velocity;
		force = Constants.gravity;
		mass = new Mass(this, 1);
	}

	public void update() {
		velocity = velocity.plus(force);
		setPosition(center().plus(velocity));
	}

	public void setPosition(Point2D c) {
		xc = c.getX();
		yc = c.getY();
	}

	public Vector2D getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}

	public double getMass() {
		return mass.getMass();
	}

	public void setRadius(double radius) {
		r = radius;
	}
}
