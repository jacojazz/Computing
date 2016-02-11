package gui;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.conic.Ellipse2D;

public class Circle extends Ellipse2D {
	double radius, mass;
	Point2D position;

	Circle(Point2D position, double radius, double mass, Vector2D velocity) {
		super(position, radius, radius);
		this.radius = radius;
		this.mass = mass;
	}
}
