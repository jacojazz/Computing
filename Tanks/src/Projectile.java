import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.conic.Circle2D;

public class Projectile extends Circle2D {
	Vector2D velocity;

	Projectile(Point2D c, double r, Vector2D velocity) {
		super(c, r);
		this.velocity = velocity;
	}

	void update() {
		velocity = velocity.plus(Game.gravity);
		xc = center().x() + velocity.getX();
		yc = center().y() + velocity.getY();
	}

	Collection<Point2D> getImpact(double leftBound, double rightBound) {
		double left = leftBound, right = rightBound;
		if (leftBound > rightBound) {
			double tempPos = left;
			left = right;
			right = tempPos;
		}
		Collection<Point2D> temp = new ArrayList<Point2D>();
		double angle = ((2 * Math.PI) / 10);
		for (double i = leftBound; i < rightBound; i += angle) {
			temp.add(point(angle * i));
		}
		return temp;
	}

	void paint(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		this.draw(g2d);
	}
}
