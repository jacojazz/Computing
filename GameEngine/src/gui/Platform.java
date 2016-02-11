package gui;

import math.geom2d.line.Line2D;

public class Platform extends Line2D {
	Materials material = Materials.Static;

	public Platform(double x1, double y1, double x2, double y2) {
		super(x1, y1, x2, y2);
	}
}
