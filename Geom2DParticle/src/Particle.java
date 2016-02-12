import java.awt.Graphics2D;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.line.Line2D;
import math.geom2d.line.StraightLine2D;

public class Particle extends Circle2D {
	Vector2D velocity;
	Vector2D force;
	Vector2D position;
	Material material;

	Particle(Point2D center, Vector2D velocity, double radius, Material material) {
		super(center, radius);
		this.velocity = velocity;
		this.material = material;

		force = new Vector2D(0, 0.05);
	}

	boolean touchingLine(Platform line) {
		double distance2Floor = line.distance(new Point2D(position.getX(), position.getY()));
		if (distance2Floor <= radius()) {
			return true;
		} else {
			return false;
		}
	}

	Vector2D bounce(Platform p) {
		double velocityAngle = velocity.angle();
		double platformAngle = p.horizontalAngle();
		double platformAngle2 = (2 * Math.PI) - platformAngle;
		double resolutionAngle = (2 * Math.PI) - (velocityAngle + platformAngle2);
		double rotate = 2 * resolutionAngle;
		return velocity.rotate(rotate);

		// return new Vector2D(velocity.getX() * p.material.getFriction(),
		// (-velocity.getY()) * p.material.getRestitution());
	}

	boolean touchingAnyLine() {
		for (Platform p : Game.platformList) {
			if (touchingLine(p)) {
				return true;
			}
		}
		return false;
	}

	void update() {
		position = new Vector2D(center());

		for (Particle b : Game.particleList) {
			if (isColliding(this, b)) {
				resolveCollision(this, b);
			}
		}

		for (Platform p : Game.platformList) {
			if (touchingLine(p)) {
				System.out.println(p);
				velocity = bounce(p);
				velocity = new Vector2D(velocity.getX(), -(Math.abs(velocity.getY()) * p.material.getRestitution()));
			}
		}

		position = position.plus(velocity);

		if (!touchingAnyLine()) {
			velocity = velocity.plus(force);
		}

		xc = position.getX();
		yc = position.getY();
	}

	boolean isColliding(Particle a, Particle b) {
		double r = a.radius() + b.radius();
		r *= r;
		return r < Math.pow(a.center().getX() + b.center().getX(), 2) + Math.pow(a.center().getY() + b.center().getY(), 2);
	}

	void resolveCollision(Particle A, Particle B) {
		Vector2D rv = B.velocity.minus(A.velocity);
		Line2D normalLine = new Line2D(A.center(), B.center());
		StraightLine2D perpendicular = normalLine.perpendicular(normalLine.point(normalLine.length() / 2));
		Vector2D normalVector = perpendicular.normal(perpendicular.length() / 2);

		double velAlongNormal = rv.dot(normalVector);

		if (velAlongNormal > 0)
			return;

		double e = Math.min(A.material.getRestitution(), B.material.getRestitution());

		double j = -(1 + e) * velAlongNormal;
		j /= 1 / 1 + 1 / 1;

		Vector2D impulse = normalVector.times(j);
		A.velocity = A.velocity.minus(impulse.times(1 / 1));
		B.velocity = B.velocity.plus(impulse.times(1 / 1));
	}

	public static Point2D getClosestPointOnSegment(Platform p, Point2D center) {
		double xDelta = p.getX2() - p.getX1();
		double yDelta = p.getY2() - p.getY1();

		double u = ((center.getX() - p.getX1()) * xDelta + (center.getY() - p.getY1()) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

		final Point2D closestPoint;
		if (u < 0) {
			closestPoint = new Point2D(p.getX1(), p.getY1());
		} else if (u > 1) {
			closestPoint = new Point2D(p.getX2(), p.getY2());
		} else {
			closestPoint = new Point2D((int) Math.round(p.getX1() + u * xDelta), (int) Math.round(p.getY1() + u * yDelta));
		}

		return closestPoint;
	}

	void paint(Graphics2D g2d) {
		fill(g2d);
	}
}
