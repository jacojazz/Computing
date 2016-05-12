import java.util.Comparator;

import math.geom2d.Point2D;

public class PointDistance implements Comparator<Point2D> {
	Point2D checkPoint;

	PointDistance(Point2D distanceTo) {
		this.checkPoint = distanceTo;
	}

	public int compare(Point2D p1, Point2D p2) {
		return (int) (p1.distance(checkPoint) - p2.distance(checkPoint));
	}
}
