package engine.game;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.line.Line2D;

public class GravityNode extends Point2D {
	double intensity;

	GravityNode(Point2D center, double intensity) {
		super(center.getX(), center.getY());
		this.intensity = intensity;
	}

	Vector2D gravityAtPoint(Particle p) {
		Line2D lineSeparation = new Line2D(p.center(), this);
		Vector2D lineDirection = lineSeparation.direction().normalize();
		if (p.distance(this) < p.radius()) {
			return new Vector2D(0, 0);
		} else {
			return lineDirection.times(inverseSquare(p));
		}
	}

	double inverseSquare(Particle p) {
		double calc = intensity / p.center().distance(this);
		if (!(calc < 0)) {
			return calc;
		} else {
			return 0;
		}
	}
}
