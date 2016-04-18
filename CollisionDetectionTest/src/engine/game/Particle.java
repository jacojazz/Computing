package engine.game;

import java.util.Timer;
import java.util.TimerTask;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.line.Line2D;
import engine.utils.Constants;

public class Particle extends Circle2D {
	private Vector2D velocity;
	private double mass;
	private boolean active = true;
	private Point2D oldPosition;
	GravityNodeParticle gnp;

	Particle(Point2D center, double radius, double mass) {
		super(center, radius);
		this.mass = mass;
		gnp = new GravityNodeParticle(center(), 1);
	}

	Particle(Point2D center, double radius, double mass, Vector2D velocity) {
		super(center, radius);
		this.mass = mass;
		this.velocity = velocity;
		gnp = new GravityNodeParticle(center(), 1);
	}

	boolean checkCollision(Particle p, Particle p2) {
		double xDif = p.center().getX() - p2.center().getX();
		double yDif = p.center().getY() - p2.center().getY();
		double distanceSquared = (xDif * xDif) + (yDif * yDif);
		boolean collision = distanceSquared <= (p.radius() + p2.radius()) * (p.radius() + p2.radius());
		return collision;
	}

	boolean inLineCollisionRange(Line2D l) {
		if (l.distance(center()) <= radius()) {
			double penetrationDepth = radius() - l.distance(center());
			Vector2D resolution = l.perpendicular(center()).direction().normalize().times(penetrationDepth);
			if (velocity.angle() > l.horizontalAngle() && velocity.angle() < (l.horizontalAngle() + Math.PI)) {
				resolution = resolution.opposite();
			}
			setPosition(center().plus(resolution));
			return true;
		} else {
			return false;
		}
	}

	boolean inParticleCollisionRange(Particle p2) {
		if (isActive() || p2.isActive()) {
			try {
				if (distance(p2.center()) < (radius() + p2.radius()) * 2) {
					return true;
				} else {
					return false;
				}
			} catch (NullPointerException e) {
				return true;
			}
		} else {
			return false;
		}
	}

	Vector2D reflect(Line2D l) {
		Vector2D n = l.perpendicular(center()).direction().normalize();
		if (center().getY() < l.point(l.project(center())).getY()) {
			n = n.opposite();
		}
		Vector2D v = velocity.minus(n.times(2 * (n.dot(velocity))));
		return new Vector2D(v.getX() * Constants.restitution, v.getY() * Constants.restitution);
	}

	public void resolveCollision(Particle p, Particle p2) {
		p.setActive(true);
		p2.setActive(true);
		Vector2D delta = new Vector2D(p.center().minus(p2.center()));
		double d = delta.norm();
		Vector2D mtd = delta.times(((p.radius() + p2.radius()) - d) / d);
		double im1 = (1 / p.getMass());
		double im2 = (1 / p2.getMass());
		p.setPosition(p.center().plus(mtd.times(im1 / (im1 + im2))));
		p2.setPosition(p2.center().minus(mtd.times(im2 / (im1 + im2))));
		Vector2D v = (p.getVelocity().minus(p2.getVelocity()));
		double vn = v.dot(mtd.normalize());
		if (vn > 0.0f)
			return;
		double i = ((-(1.0f + Constants.restitution) * vn) / (im1 + im2));
		Vector2D impulse = mtd.normalize().times(i);
		p.setVelocity(p.getVelocity().plus(impulse.times(im1)));
		p2.setVelocity(p2.getVelocity().minus(impulse.times(im2)));
	}

	double tolerance(double penetrationDepth, double radius) {
		double result = penetrationDepth / radius;
		if (result > 1) {
			return 1;
		} else {
			return result;
		}
	}

	Vector2D getForceParticle(Particle p) {
		if (Game.gravityType == 2) {
			Vector2D temp = new Vector2D(0, 0);
			for (int gNodeIterator = 0; gNodeIterator < Game.gList.size(); gNodeIterator++) {
				GravityNode g = Game.gList.get(gNodeIterator);
				temp = temp.plus(g.gravityAtParticle(p));
			}
			for (int pNodeIterator = 0; pNodeIterator < Game.pList.size(); pNodeIterator++) {
				Particle p2 = Game.pList.get(pNodeIterator);
				if (!p.equals(p2)) {
					temp = temp.plus(p2.gnp.gravityAtParticle(p));
				}
			}
			return temp;
		} else {
			return Game.getGravity();
		}
	}

	Vector2D getForcePoint(Point2D p) {
		if (Game.gravityType == 2) {
			Vector2D temp = new Vector2D(0, 0);
			for (int gNodeIterator = 0; gNodeIterator < Game.gList.size(); gNodeIterator++) {
				GravityNode g = Game.gList.get(gNodeIterator);
				temp = temp.plus(g.gravityAtPoint(p));
			}
			return temp;
		} else {
			return Game.getGravity();
		}
	}

	void checkActive() {
		if (Game.frames % Game.TARGET_FPS == 0 && Game.gravityType != 2) {
			oldPosition = center();
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run() {
					if (oldPosition.equals(center())) {
						setActive(false);
					}
				}
			}, 100);
		}
	}

	void update() {
		gnp.update(center(), mass / 10);
		for (int particle2Iterator = 0; particle2Iterator < Game.pList.size(); particle2Iterator++) {
			Particle p2 = Game.pList.get(particle2Iterator);
			if (inParticleCollisionRange(p2)) {
				if (checkCollision(this, p2) && !equals(p2)) {
					resolveCollision(this, p2);
				}
			}
		}

		for (int lineIterator = 0; lineIterator < Game.lList.size(); lineIterator++) {
			Line2D l = Game.lList.get(lineIterator);
			if (inLineCollisionRange(l)) {
				velocity = reflect(l);
			}
		}

		velocity = velocity.plus(getForceParticle(this));
		Point2D p = center().plus(velocity);
		setPosition(p.getX(), p.getY());
		checkActive();
	}

	public void setPosition(double x, double y) {
		xc = x;
		yc = y;
		theta = velocity.angle();
	}

	public void setPosition(Object obj) {
		if (obj instanceof Vector2D) {
			Vector2D v = (Vector2D) obj;
			xc = v.getX();
			yc = v.getY();
		} else if (obj instanceof Point2D) {
			Point2D p = (Point2D) obj;
			xc = p.getX();
			yc = p.getY();
		} else {
			return;
		}
	}

	public Vector2D getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setRadius(double radius) {
		r = radius;
	}
}
