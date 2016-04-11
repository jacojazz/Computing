package engine.game;

import math.geom2d.Point2D;

public class GravityNode extends Point2D {
	double intensity;
	double radius;

	GravityNode(Point2D center, double radius, double intensity) {
		super(center.getX(), center.getY());
		this.radius = radius;
		this.intensity = intensity;
	}
}
