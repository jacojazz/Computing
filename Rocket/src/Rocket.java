import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

import math.geom2d.Point2D;
import math.geom2d.curve.GeneralPath2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.polygon.SimplePolygon2D;
import math.geom2d.spline.CubicBezierCurve2D;

public class Rocket {
	SimplePolygon2D main = new SimplePolygon2D();
	SimplePolygon2D engine = new SimplePolygon2D();
	CubicBezierCurve2D noseLeft = new CubicBezierCurve2D(new Point2D(5, 50), new Point2D(5, 40), new Point2D(25, 0), new Point2D(35, 0));
	CubicBezierCurve2D noseRight = new CubicBezierCurve2D(new Point2D(65, 50), new Point2D(65, 40), new Point2D(45, 0), new Point2D(35, 0));
	ArrayList<LineSegment2D> lineArray = new ArrayList<LineSegment2D>();
	SimplePolygon2D rcsLeft = new SimplePolygon2D();
	SimplePolygon2D rcsRight = new SimplePolygon2D();
	GeneralPath2D shape = new GeneralPath2D();
	Point2D center;

	public Rocket() {
		main.addVertex(new Point2D(5, 50));
		main.addVertex(new Point2D(5, 300));
		main.addVertex(new Point2D(65, 300));
		main.addVertex(new Point2D(65, 50));

		rcsLeft.addVertex(new Point2D(5, 170));
		rcsLeft.addVertex(new Point2D(0, 172));
		rcsLeft.addVertex(new Point2D(0, 178));
		rcsLeft.addVertex(new Point2D(5, 180));

		rcsRight.addVertex(new Point2D(65, 170));
		rcsRight.addVertex(new Point2D(70, 172));
		rcsRight.addVertex(new Point2D(70, 178));
		rcsRight.addVertex(new Point2D(65, 180));

		engine.addVertex(new Point2D(15, 300));
		engine.addVertex(new Point2D(7, 310));
		engine.addVertex(new Point2D(63, 310));
		engine.addVertex(new Point2D(55, 300));

		lineArray.add(new LineSegment2D(new Point2D(5, 70), new Point2D(65, 70)));
		lineArray.add(new LineSegment2D(new Point2D(5, 80), new Point2D(65, 80)));
		lineArray.add(new LineSegment2D(new Point2D(5, 100), new Point2D(65, 100)));
		lineArray.add(new LineSegment2D(new Point2D(5, 200), new Point2D(65, 200)));

		center = main.centroid();
	}

	public void paint(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		main.draw(g2d);
		engine.fill(g2d);
		for (Iterator<LineSegment2D> lineIterator = lineArray.iterator(); lineIterator.hasNext();) {
			lineIterator.next().draw(g2d);
		}
		noseLeft.draw(g2d);
		noseRight.draw(g2d);
		rcsLeft.draw(g2d);
		rcsRight.draw(g2d);
	}
}
