package gui;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.conic.Circle2D;

public class Particle extends Circle2D {
	Materials material;
	MassData mass_data;
	double GRAVITY_SCALE = 0.7;
	Vector2D position;
	Vector2D velocity;
	Vector2D force = new Vector2D(0, 0);

	Particle(Point2D center, double radius, Vector2D velocity, Materials material) {
		super(center, radius);
		this.velocity = velocity;
		this.material = material;

		mass_data = new MassData(calculateMass());

		if (material != Materials.Static) {
			force = new Vector2D(0, GRAVITY_SCALE);
		}

		position = new Vector2D(center);
	}

	private double calculateMass() {
		double area = Math.PI * Math.pow(radius(), 2);
		return material.density() * area;
	}

	double getMass() {
		return mass_data.getMass();
	}

	double getInverseMass() {
		return mass_data.getInv_mass();
	}

	void update() {
		position = new Vector2D(center());

		if (Game.floor.distance(center()) <= radius()) {
			velocity = new Vector2D(velocity.getX() * Game.floor.material.friction(), Game.floor.distance(center()) - radius());
		} else {
			velocity = velocity.plus(force);
		}

		position = position.plus(velocity);
		xc = position.x();
		yc = position.y();
	}
}
