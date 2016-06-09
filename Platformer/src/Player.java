import java.util.Iterator;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.line.Line2D;
import math.geom2d.line.LineSegment2D;

public class Player extends Circle2D {
	Vector2D velocity;
	Vector2D force;
	Box2D cBounds;
	boolean lineCollision = false;
	boolean doubleJump = true;
	int jumpCount = Integer.MAX_VALUE;

	Player(Point2D c, double r) {
		this(c, r, new Vector2D(0, 0), Constants.NORMAL_GRAVITY);
	}

	Player(Point2D c, double r, Vector2D v) {
		this(c, r, v, new Vector2D(0, 0));
	}

	Player(Point2D c, double r, Vector2D v, Vector2D f) {
		super(c, r);
		this.velocity = v;
		this.force = f;

		cBounds = this.buffer(r * 2).boundingBox();
	}

	void update() {
		if (InputHandler.left) {
			moveLeft(true);
		} else {
			moveLeft(false);
			if (InputHandler.right) {
				moveRight(true);
			} else {
				moveRight(false);
			}
		}

		velocity = velocity.plus(force);
		setPosition(center().plus(velocity));
		cBounds = this.buffer(r * 2).boundingBox();

		for (Iterator<LineSegment2D> lIterator = Game.bounds.edges().iterator(); lIterator.hasNext();) {
			LineSegment2D l = lIterator.next();
			lineCollision = inLineCollisionRange(l, true);
			if (lineCollision) {
				reflect(l);
			}
		}
	}

	boolean inLineCollisionRange(LineSegment2D l, boolean setPosition) {
		if (center().distance(l.point(l.positionOnLine(center()))) <= radius()) {
			if (setPosition) {
				double penetrationDepth = 0;
				if (center().distance(l.point(l.positionOnLine(center()))) < radius()) {
					penetrationDepth = Math.abs(radius() - center().distance(l.point(l.positionOnLine(center()))));
				} else if (center().distance(l.point(l.positionOnLine(center()))) >= radius()) {
					penetrationDepth = 0;
				}
				Vector2D resolutionVector = l.normal(l.positionOnLine(center()));
				Vector2D resolution;
				double dotCalc = new Line2D(center(), l.point(l.positionOnLine(center()))).direction().dot(resolutionVector);
				if (dotCalc >= 0) {
					resolution = resolutionVector.normalize().opposite().times(penetrationDepth);
					setPosition(center().plus(resolution));
				} else if (dotCalc < 0) {
					resolution = resolutionVector.normalize().times(penetrationDepth);
					setPosition(center().plus(resolution));
				}
			}
			if (new Line2D(center(), l.point(l.position(center()))).horizontalAngle() == (Math.PI / 2)) {
				jumpCount = 0;
			}
			return true;
		} else {
			return false;
		}
	}

	void reflect(LineSegment2D l) {
		double angle = new Line2D(center(), l.point(l.position(center()))).horizontalAngle();
		Vector2D n = l.normal(l.position(center())).normalize();
		Vector2D v = velocity.minus(n.times(2 * (n.dot(velocity))));
		if (angle == 0 || angle == Math.PI) {
			velocity = new Vector2D(0, Constants.NORMAL_GRAVITY.getY());
		} else {
			velocity = new Vector2D(v.getX() * Constants.restitution, v.getY() * Constants.restitution);
		}
	}

	void moveLeft(boolean active) {
		if (active) {
			velocity = new Vector2D(-5, velocity.getY());
		} else {
			velocity = new Vector2D(velocity.getX() * 0.965, velocity.getY());
		}
	}

	void moveRight(boolean active) {
		if (active) {
			velocity = new Vector2D(5, velocity.getY());
		} else {
			velocity = new Vector2D(velocity.getX() * 0.965, velocity.getY());
		}
	}

	void jump() {
		if ((jumpCount > 0 && jumpCount < 2 && doubleJump) ^ jumpCount == 0) {
			jumpCount++;
			velocity = new Vector2D(0, -15);
		}
	}

	void setPosition(Object obj) {
		if (obj instanceof Point2D) {
			xc = ((Point2D) obj).getX();
			yc = ((Point2D) obj).getY();
		} else if (obj instanceof Vector2D) {
			xc = ((Vector2D) obj).getX();
			yc = ((Vector2D) obj).getY();
		}
	}
}
