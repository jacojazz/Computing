import math.geom2d.Point2D;
import math.geom2d.polygon.Rectangle2D;

public class Platform extends Rectangle2D {
	Platform(Point2D o, double width, double height) {
		super(o.getX(), o.getY(), width, height);
	}

	Platform(Point2D i, Point2D f) {
		super(i, f);
	}
}
