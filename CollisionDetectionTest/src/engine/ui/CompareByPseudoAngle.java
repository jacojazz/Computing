package engine.ui;

import java.util.Comparator;

import math.geom2d.Angle2D;
import math.geom2d.Point2D;

public class CompareByPseudoAngle implements Comparator<Point2D> {
	Point2D basePoint;

	public CompareByPseudoAngle(Point2D base) {
		this.basePoint = base;
	}

	public int compare(Point2D point1, Point2D point2) {
		double angle1 = Angle2D.pseudoAngle(basePoint, point1);
		double angle2 = Angle2D.pseudoAngle(basePoint, point2);

		if (angle1 < angle2)
			return -1;
		if (angle1 > angle2)
			return +1;
		return 0;
	}
}
