package engine.collision;

import java.util.Iterator;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.line.Line2D;
import math.geom2d.line.LineSegment2D;
import engine.Game;
import engine.geometry.Particle;
import engine.utils.Constants;

public class CollisionHandler implements Runnable {
	Thread t;

	public void update() {
		t = new Thread(this);
		t.start();
	}

	void resolveCollision(Particle p, Particle p2) {
		Vector2D delta = new Vector2D(p.center().minus(p2.center()));
		double d = delta.norm();
		Vector2D mtd = delta.times(((p.radius() + p2.radius()) - d) / d);
		if (mtd.norm() == 0) {
			return;
		}
		double im1 = 1 / p.getMass();
		double im2 = 1 / p2.getMass();
		p.setPosition(p.center().plus(mtd.times(im1 / (im1 + im2))));
		p2.setPosition(p2.center().minus(mtd.times(im2 / (im1 + im2))));
		Vector2D v = (p.getVelocity().minus(p2.getVelocity()));
		double vn = v.dot(mtd.normalize());
		if (vn > 0.0)
			return;
		double i = (-(1.0 + Constants.restitution) * vn) / (im1 + im2);
		Vector2D impulse = mtd.normalize().times(i);
		p.setVelocity(p.getVelocity().plus(impulse.times(im1)));
		p2.setVelocity(p2.getVelocity().minus(impulse.times(im2)));
		for (Iterator<LineSegment2D> lIterator = Game.lList.iterator(); lIterator.hasNext();) {
			LineSegment2D l = lIterator.next();
			inLineCollisionRange(p, l, true);
			inLineCollisionRange(p2, l, true);
		}
	}

	void reflect(Particle p, LineSegment2D l) {
		Vector2D n = l.normal(l.position(p.center())).normalize();
		Vector2D v = p.getVelocity().minus(n.times(2 * (n.dot(p.getVelocity()))));
		p.setVelocity(new Vector2D(v.getX() * Constants.restitution, v.getY() * Constants.restitution));
	}

	boolean checkCollision(Particle p, Particle p2) {
		if (p.equals(p2)) {
			return false;
		}

		if (p.center().distance(p2.center()) <= p.radius() + p2.radius()) {
			return true;
		} else {
			return false;
		}
	}

	boolean inLineCollisionRange(Particle p, LineSegment2D l, boolean setPosition) {
		if (p.center().distance(l.point(l.positionOnLine(p.center()))) <= p.radius()) {
			if (setPosition) {
				double penetrationDepth = 0;
				if (p.center().distance(l.point(l.positionOnLine(p.center()))) < p.radius()) {
					penetrationDepth = Math.abs(p.radius() - p.center().distance(l.point(l.positionOnLine(p.center()))));
				} else if (p.center().distance(l.point(l.positionOnLine(p.center()))) >= p.radius()) {
					penetrationDepth = 0;
				}
				Vector2D resolutionVector = l.normal(l.positionOnLine(p.center()));
				Vector2D resolution;
				double dotCalc = new Line2D(p.center(), l.point(l.positionOnLine(p.center()))).direction().dot(resolutionVector);
				if (dotCalc >= 0) {
					resolution = resolutionVector.normalize().opposite().times(penetrationDepth);
					p.setPosition(p.center().plus(resolution));
				} else if (dotCalc < 0) {
					resolution = resolutionVector.normalize().times(penetrationDepth);
					p.setPosition(p.center().plus(resolution));
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public void run() {
		for (Iterator<Particle> particleIterator = Game.pList.iterator(); particleIterator.hasNext();) {
			Particle p = particleIterator.next();
			for (Iterator<LineSegment2D> lineIterator = Game.lList.iterator(); lineIterator.hasNext();) {
				LineSegment2D l = lineIterator.next();
				if (inLineCollisionRange(p, l, true)) {
					reflect(p, l);
				}
				if (!p.intersections(l).isEmpty()) {
					Point2D closestPoint = l.point(l.positionOnLine(p.center()));
					Vector2D directionVector = new LineSegment2D(closestPoint, p.center()).direction().normalize().times(p.radius());
					p.setPosition(new Point2D(closestPoint.plus(directionVector).getX(), closestPoint.plus(directionVector).getY()));
				}
			}

			for (Iterator<Particle> particle2Iterator = Game.pList.iterator(); particle2Iterator.hasNext();) {
				Particle p2 = particle2Iterator.next();
				if (checkCollision(p, p2) && !p.equals(p2)) {
					resolveCollision(p, p2);
				}
			}
		}
	}
}
