package engine.game;

import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.line.Line2D;

public class GravityNode extends Point2D {
	public double intensity;
	boolean repulsor;

	GravityNode(Point2D center, double intensity, boolean repulsor) {
		super(center.getX(), center.getY());
		this.intensity = intensity;
		this.repulsor = repulsor;
	}

	Collection<Point2D> getPointsAroundPoint(int divisions, int radius) {
		Collection<Point2D> temp = new ArrayList<Point2D>();
		for (int i = 0; i < divisions; i++) {
			double angle = ((2 * Math.PI) / divisions) * i;
			temp.add(new Circle2D(this, radius).point(angle));
		}
		return temp;
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

	public void setPosition(Point2D p) {
		x = p.getX();
		y = p.getY();
	}
}
