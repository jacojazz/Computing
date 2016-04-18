package engine.game;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.line.Line2D;

public class GravityNodeParticle extends Point2D {
	double intensity;

	GravityNodeParticle(Point2D center, double intensity) {
		super(center.getX(), center.getY());
		this.intensity = intensity / 100;
	}

	Vector2D gravityAtParticle(Particle p) {
		Line2D lineSeparation = new Line2D(p.center(), this);
		Vector2D lineDirection = lineSeparation.direction().normalize();

		if (p.distance(this) < p.radius()) {
			return new Vector2D(0, 0);
		} else {
			return lineDirection.times(inverseSquare(p.center()));
		}
	}

	double inverseSquare(Point2D p) {
		double calc = intensity / p.distance(this);
		if (!(calc < 0)) {
			return calc;
		} else {
			return 0;
		}
	}

	void update(Point2D center, double intensity) {
		this.intensity = intensity / 100;
		x = center.getX();
		y = center.getY();
	}
}
