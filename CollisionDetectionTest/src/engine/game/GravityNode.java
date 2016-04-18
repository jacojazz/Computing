package engine.game;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.line.Line2D;

public class GravityNode extends Point2D {
	double intensity;
	boolean repulsor;

	GravityNode(Point2D center, double intensity, boolean repulsor) {
		super(center.getX(), center.getY());
		this.intensity = intensity;
		this.repulsor = repulsor;
	}

	Vector2D gravityAtParticle(Particle p) {
		Line2D lineSeparation = new Line2D(p.center(), this);
		Vector2D lineDirection = lineSeparation.direction().normalize();

		if (repulsor) {
			lineDirection = lineDirection.opposite();
		}

		if (p.distance(this) < p.radius()) {
			return new Vector2D(0, 0);
		} else {
			return lineDirection.times(inverseSquare(p.center()));
		}
	}

	Vector2D gravityAtPoint(Point2D p) {
		Line2D lineSeparation = new Line2D(p, this);
		Vector2D lineDirection = lineSeparation.direction().normalize();

		if (repulsor) {
			lineDirection = lineDirection.opposite();
		}

		return lineDirection.times(inverseSquare(p));
	}

	double inverseSquare(Point2D p) {
		double calc = intensity / p.distance(this);
		if (!(calc < 0)) {
			return calc;
		} else {
			return 0;
		}
	}

	void setPosition(Point2D p) {
		x = p.getX();
		y = p.getY();
	}
}
