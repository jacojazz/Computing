package engine.collision;

import java.util.Iterator;

import math.geom2d.Vector2D;
import math.geom2d.line.Line2D;
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
	}

	void reflect(Particle p, Line2D l) {
		Vector2D n = l.normal(l.position(p.center())).normalize();
		Vector2D v = p.getVelocity().minus(n.times(2 * (n.dot(p.getVelocity()))));
		p.setVelocity(new Vector2D(v.getX() * Constants.restitution, v.getY() * Constants.restitution));
	}

	boolean checkCollision(Particle p, Particle p2) {
		double xDif = p.center().getX() - p2.center().getX();
		double yDif = p.center().getY() - p2.center().getY();
		double distanceSquared = (xDif * xDif) + (yDif * yDif);
		boolean collision = distanceSquared <= (p.radius() + p2.radius()) * (p.radius() + p2.radius());
		return collision;
	}

	boolean inLineCollisionRange(Particle p, Line2D l, boolean setPosition) {
		if (l.distance(p.center()) <= p.radius()) {
			if (setPosition) {
				double penetrationDepth = 0;
				if (l.distance(p.center()) < p.radius()) {
					penetrationDepth = Math.abs(p.radius() - l.distance(p.center()));
				} else if (l.distance(p.center()) >= p.radius()) {
					penetrationDepth = 0;
				}
				Vector2D resolutionVector = l.normal(l.position(p.center()));
				Vector2D resolution;
				double dotCalc = new Line2D(p.center(), l.point(l.position(p.center()))).direction().dot(resolutionVector);
				if (dotCalc > 0) {
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
			for (Iterator<Line2D> lineIterator = Game.lList.iterator(); lineIterator.hasNext();) {
				Line2D l = lineIterator.next();
				if (inLineCollisionRange(p, l, true)) {
					reflect(p, l);
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
