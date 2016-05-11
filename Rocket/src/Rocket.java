import java.awt.Color;
import java.awt.Graphics2D;

import math.geom2d.Point2D;
import math.geom2d.line.LineArc2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.polygon.SimplePolygon2D;

public class Rocket {
	Point2D center = new Point2D(0, 0);
	SimplePolygon2D main = new SimplePolygon2D();
	SimplePolygon2D engine = new SimplePolygon2D();
	LineArc2D leftNose = new LineArc2D(new Point2D(100, 100), new Point2D(200, 200), 30, 30);

	public Rocket() {
		main.addVertex(new Point2D(30, 0));
		main.addVertex(new Point2D(0, 50));
		main.addVertex(new Point2D(0, 300));
		main.addVertex(new Point2D(60, 300));
		main.addVertex(new Point2D(60, 50));
		engine.addVertex(new Point2D(10, 300));
		engine.addVertex(new Point2D(2, 310));
		engine.addVertex(new Point2D(58, 310));
		engine.addVertex(new Point2D(50, 300));
	}

	public void paint(Graphics2D g2d) {
		g2d.setColor(Color.WHITE);
		main.fill(g2d);
		g2d.setColor(Color.BLACK);
		main.draw(g2d);
		g2d.setColor(Color.GRAY);
		engine.fill(g2d);
		g2d.setColor(Color.BLACK);
		leftNose.draw(g2d);
	}
}
