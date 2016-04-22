package engine.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import math.geom2d.Point2D;
import math.geom2d.polygon.Polygon2D;
import math.geom2d.polygon.SimplePolygon2D;
import engine.game.Game;

public class ConvexHull {
	public static Polygon2D convexHull(ArrayList<Point2D> points) {
		int nbPoints = points.size();
		Point2D lowestPoint = null;
		double lowestY = Game.HEIGHT;
		for (Point2D point : points) {
			double y = point.y();
			if (y < lowestY) {
				lowestPoint = point;
				lowestY = y;
			}
		}

		Comparator<Point2D> comparator = new CompareByPseudoAngle(lowestPoint);

		ArrayList<Point2D> sorted = new ArrayList<Point2D>(points);
		Collections.sort(sorted, comparator);

		int m = 2;
		for (int i = 3; i < nbPoints; i++) {
			while (Point2D.ccw(sorted.get(m), sorted.get(m - 1), sorted.get(i)) >= 0)
				m--;
			m++;
			Collections.swap(sorted, m, i);
		}

		List<Point2D> hull = sorted.subList(0, Math.min(m + 1, nbPoints));
		return new SimplePolygon2D(hull);
	}
}
